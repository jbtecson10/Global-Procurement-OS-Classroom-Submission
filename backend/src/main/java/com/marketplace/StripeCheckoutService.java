package com.marketplace;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class StripeCheckoutService {
    record CheckoutSession(String id, String url) {}
    private final String secretKey;
    private final String webhookSecret;
    private final String publicUrl;
    private final ObjectMapper json;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    StripeCheckoutService(@Value("${app.stripe.secret-key:}") String secretKey,
                          @Value("${app.stripe.webhook-secret:}") String webhookSecret,
                          @Value("${app.public-url:http://localhost:3000}") String publicUrl,
                          ObjectMapper json) {
        this.secretKey = secretKey;
        this.webhookSecret = webhookSecret;
        this.publicUrl = publicUrl.replaceAll("/$", "");
        this.json = json;
    }

    boolean enabled() { return secretKey.startsWith("sk_test_") || secretKey.startsWith("sk_live_") ? webhookSecret.startsWith("whsec_") : false; }

    CheckoutSession createSession(MarketplaceOrder order) {
        if (!enabled()) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Online checkout is not configured. Use the local demo payment or contact the marketplace administrator.");
        Product product = order.getProduct();
        long unitAmount;
        try { unitAmount = product.getPrice().movePointRight(2).longValueExact(); }
        catch (ArithmeticException invalidAmount) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be represented by the selected currency."); }
        String success = publicUrl + "/?payment=success&order=" + order.getId() + "&session_id={CHECKOUT_SESSION_ID}";
        String cancel = publicUrl + "/?payment=cancel&order=" + order.getId();
        Map<String,String> fields = Map.ofEntries(
                Map.entry("mode", "payment"), Map.entry("client_reference_id", String.valueOf(order.getId())),
                Map.entry("success_url", success), Map.entry("cancel_url", cancel),
                Map.entry("line_items[0][price_data][currency]", product.getCurrency().toLowerCase()),
                Map.entry("line_items[0][price_data][unit_amount]", String.valueOf(unitAmount)),
                Map.entry("line_items[0][price_data][product_data][name]", product.getName()),
                Map.entry("line_items[0][quantity]", String.valueOf(order.getQuantity())),
                Map.entry("metadata[order_id]", String.valueOf(order.getId())));
        String body = fields.entrySet().stream().map(e -> enc(e.getKey()) + "=" + enc(e.getValue())).collect(java.util.stream.Collectors.joining("&"));
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.stripe.com/v1/checkout/sessions"))
                .timeout(Duration.ofSeconds(20)).header("Authorization", "Bearer " + secretKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Idempotency-Key", "marketplace-order-" + order.getId() + "-checkout-v1")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode payload = json.readTree(response.body());
            if (response.statusCode() < 200 || response.statusCode() >= 300)
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Payment provider could not start checkout. Check the Stripe configuration and try again.");
            String id = payload.path("id").asText("");
            String url = payload.path("url").asText("");
            if (id.isBlank() || !url.startsWith("https://checkout.stripe.com/"))
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Payment provider returned an invalid checkout session.");
            return new CheckoutSession(id, url);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Could not connect to the payment provider.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Checkout was interrupted. Please try again.");
        }
    }

    boolean verifyWebhook(String rawBody, String signatureHeader) {
        if (!enabled() || signatureHeader == null) return false;
        String timestamp = null;
        List<String> signatures = new ArrayList<>();
        for (String part : signatureHeader.split(",")) {
            String[] pair = part.trim().split("=", 2);
            if (pair.length != 2) continue;
            if (pair[0].equals("t")) timestamp = pair[1];
            if (pair[0].equals("v1")) signatures.add(pair[1]);
        }
        if (timestamp == null || signatures.isEmpty()) return false;
        long epoch;
        try { epoch = Long.parseLong(timestamp); } catch (NumberFormatException invalid) { return false; }
        if (Math.abs(Instant.now().getEpochSecond() - epoch) > 300) return false;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] digest = mac.doFinal((timestamp + "." + rawBody).getBytes(StandardCharsets.UTF_8));
            byte[] expected = HexFormat.of().formatHex(digest).getBytes(StandardCharsets.US_ASCII);
            return signatures.stream().anyMatch(signature -> MessageDigest.isEqual(expected, signature.getBytes(StandardCharsets.US_ASCII)));
        } catch (Exception invalidSignature) { return false; }
    }

    private static String enc(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }
}
