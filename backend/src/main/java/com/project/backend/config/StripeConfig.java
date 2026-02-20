package com.project.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Configuration
public class StripeConfig {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.publishable.key}")
    private String publishableKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    /**
     * Initialises the Stripe SDK with your secret key at startup.
     * Fails fast if any required key is missing or still a placeholder —
     * better to crash on boot than to fail silently at runtime.
     */
    @PostConstruct
    public void init() {
        validateKeys();
        Stripe.apiKey = secretKey;
        log.info("Stripe SDK initialised successfully.");
    }

    private void validateKeys() {
        if (!StringUtils.hasText(secretKey) || secretKey.startsWith("sk_test_YOUR")) {
            throw new IllegalStateException(
                "stripe.secret.key is not configured. " +
                "Set it in application.properties or as an environment variable.");
        }
        if (!StringUtils.hasText(publishableKey) || publishableKey.startsWith("pk_test_YOUR")) {
            throw new IllegalStateException(
                "stripe.publishable.key is not configured. " +
                "Set it in application.properties or as an environment variable.");
        }
        if (!StringUtils.hasText(webhookSecret) || webhookSecret.startsWith("whsec_YOUR")) {
            throw new IllegalStateException(
                "stripe.webhook.secret is not configured. " +
                "Set it in application.properties or as an environment variable.");
        }

        // Warn if using live keys in a non-prod looking environment
        if (secretKey.startsWith("sk_live_")) {
            log.warn("WARNING: Using Stripe LIVE secret key. " +
                     "Ensure this is intentional and you are in production.");
        }
    }
}