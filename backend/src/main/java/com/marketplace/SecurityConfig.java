package com.marketplace;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }

    @Bean UserDetailsService userDetailsService(AccountRepository accounts,
            @Value("${app.demo-data-enabled:false}") boolean demoEnabled) {
        return email -> {
            Account account=accounts.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
            if (!demoEnabled && account.getEmail().endsWith("@globalprocurement.test"))
                throw new UsernameNotFoundException("Demo account unavailable");
            return account;
        };
    }

    @Bean SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/health", "/api/demo/status", "/api/payments/status", "/api/payments/stripe/webhook", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/buyer/matches").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/buyer/matches/image").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product-images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/buyer/search").hasRole("BUYER")
                        .requestMatchers("/api/supplier/**").hasRole("SUPPLIER")
                        .requestMatchers("/api/buyer/**").hasRole("BUYER")
                        .requestMatchers("/api/payments/webhook/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/account/profile").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean CorsConfigurationSource cors(@Value("${app.cors-origin}") String origin) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of(origin));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "X-Payment-Webhook-Secret", "Stripe-Signature"));
        config.setExposedHeaders(java.util.List.of("WWW-Authenticate"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
