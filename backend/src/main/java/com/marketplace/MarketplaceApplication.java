package com.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class MarketplaceApplication {
    public static void main(String[] args) {
        configurePostgresUrl();
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    private static void configurePostgresUrl() {
        String configured = System.getenv("DATABASE_URL");
        if (configured == null || (!configured.startsWith("postgres://") && !configured.startsWith("postgresql://"))) return;
        try {
            URI uri = URI.create(configured);
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + (uri.getPort() < 0 ? 5432 : uri.getPort())
                    + uri.getRawPath() + (uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery());
            System.setProperty("spring.datasource.url", jdbcUrl);
            if (uri.getRawUserInfo() != null) {
                String[] credentials = uri.getRawUserInfo().split(":", 2);
                System.setProperty("spring.datasource.username", decode(credentials[0]));
                if (credentials.length > 1) System.setProperty("spring.datasource.password", decode(credentials[1]));
            }
        } catch (RuntimeException invalidUrl) {
            throw new IllegalStateException("DATABASE_URL must be a valid PostgreSQL or JDBC URL", invalidUrl);
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value.replace("+", "%2B"), StandardCharsets.UTF_8);
    }
}
