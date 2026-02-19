package com.project.backend.config;

import org.springframework.beans.factory.annotation.Configurable;
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.stripe.Stripe;

import lombok.Value;
@Configurable
public class StripeConfig {

    public StripeConfig(@Value("${stripe.secret.key}") String secretKey){
        Stripe.apiKey = secretKey;
    }

    // @Bean
    // public WebMvcConfigurer corsConfigure(@Value("${app.frontend.url:http://localhost:3000}") String frontendUrl){
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry){
    //             registry.addMapping(pathPattern"/""")
    //             .allowedOrigins(frontendUrl)
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .allowedHeader("*")
    //             .allowedCredentials(true)
    //             .maxAge(3600);
    //         }
    //     };
    // }
}

























// @Value("${stripe.api.key}")
    // private String apiKey;

    // @Value("${stripe.api.publishable-key}")
    // private String publishableKey;

    // @PostConstruct()
    // public void init(){
    //     Stripe.apiKey = apiKey;
    // }

    // @Value("${stripe.secret-key}")
    // private String stripeSecretKey;

    // @Value("${stripe.webhook-secret}")
    // private String webhookSecret;

    // private final JournalService journalService;

    // @PostConstruct
    // void init() {
    //     Stripe.apiKey = stripeSecretKey;
    // }
