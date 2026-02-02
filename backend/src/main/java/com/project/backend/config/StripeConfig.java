package com.project.backend.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;


@Configurable
@Getter
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${stripe.api.publishable-key}")
    private String publishableKey;

    @PostConstruct()
    public void init(){
        Stripe.apiKey = apiKey;
    }
}
