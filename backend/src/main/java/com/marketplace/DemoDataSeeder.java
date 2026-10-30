package com.marketplace;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
class DemoDataSeeder {
    static final String BUYER_EMAIL = "buyer.demo@globalprocurement.test";
    static final String SUPPLIER_EMAIL = "supplier.demo@globalprocurement.test";

    @Bean CommandLineRunner seedMarketplaceDemo(AccountRepository accounts, ProductRepository products,
            OrderRepository orders, PasswordEncoder passwords,
            @Value("${app.demo-data-enabled:false}") boolean enabled) {
        return args -> { if (enabled) seed(accounts, products, orders, passwords); };
    }

    @Transactional
    void seed(AccountRepository accounts, ProductRepository products, OrderRepository orders, PasswordEncoder passwords) {
        Account buyer=accounts.findByEmail(BUYER_EMAIL).orElseGet(() -> accounts.save(new Account(
                BUYER_EMAIL,"Alex Morgan",passwords.encode("BuyerDemo2026!"),Account.Role.BUYER,"Northstar Retail (demo)")));
        Account supplier=accounts.findByEmail(SUPPLIER_EMAIL).orElseGet(() -> accounts.save(new Account(
                SUPPLIER_EMAIL,"Mei Lin",passwords.encode("SupplierDemo2026!"),Account.Role.SUPPLIER,"Jade Harbor Manufacturing (demo)")));

        Product tote=products.findBySupplierIdOrderByIdDesc(supplier.getId()).stream()
                .filter(p -> p.getCode().equals("DEMO-TOTE-01")).findFirst().orElseGet(() -> products.save(new Product(
                        supplier,"DEMO-TOTE-01","Organic cotton market tote",
                        "Reusable 12 oz cotton canvas tote with reinforced handles. Sample listing for the buyer-to-factory workflow.",
                        "38 × 42 cm","Natural / custom","340 g","/products/demo-tote.svg",new BigDecimal("4.80"),"USD")));
        products.findBySupplierIdOrderByIdDesc(supplier.getId()).stream().filter(p -> p.getCode().equals("DEMO-BOTTLE-01"))
                .findFirst().orElseGet(() -> products.save(new Product(supplier,"DEMO-BOTTLE-01","Stainless steel travel bottle",
                        "Double-wall stainless bottle with custom color and logo options. Sample factory catalog listing.",
                        "500 ml","Brushed steel / custom","285 g","/products/demo-bottle.svg",new BigDecimal("8.25"),"USD")));
        products.findBySupplierIdOrderByIdDesc(supplier.getId()).stream().filter(p -> p.getCode().equals("DEMO-BOX-01"))
                .findFirst().orElseGet(() -> products.save(new Product(supplier,"DEMO-BOX-01","Recycled mailer box set",
                        "Recycled corrugated mailer boxes for direct-to-consumer shipping. Sample listing with custom print options.",
                        "25 × 18 × 8 cm","Kraft brown","210 g","/products/demo-box.svg",new BigDecimal("1.35"),"USD")));

        boolean hasDemoOrder=orders.findByBuyerIdOrderByCreatedAtDesc(buyer.getId()).stream()
                .anyMatch(o -> o.getSupplier().getId().equals(supplier.getId()));
        if (!hasDemoOrder) {
            MarketplaceOrder order=orders.save(new MarketplaceOrder(buyer,supplier,tote,500));
            order.setSpecifications("style=Classic shopper; design=Northstar sample mark; color=Natural; client=Demo recipient");
            order.setShippingAddress("Demo address — replace with a real delivery address before any real order.");
            order.setStatus(MarketplaceOrder.Status.INQUIRY_SENT);
            orders.save(order);
            MarketplaceOrder payment=orders.save(new MarketplaceOrder(buyer,supplier,tote,250));
            payment.setSpecifications("style=Classic shopper; design=Sample print; color=Natural; client=Demo recipient");
            payment.setShippingAddress("Demo address — no shipment will be sent.");
            payment.setStatus(MarketplaceOrder.Status.AWAITING_PAYMENT);
            orders.save(payment);
        }
    }
}
