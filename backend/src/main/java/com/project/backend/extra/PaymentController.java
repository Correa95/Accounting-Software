package com.project.backend.extra;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;


@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
    @PostMapping
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> request) {
        Long amount = Long.valueOf(request.get("amount").toString());
        String currency = request.get("currency").toString();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount(amount)
        .setCurrency(currency)
        .build();

        PaymentIntent intent = PaymentIntent.create(params);
        log.info("Payment intent Created...");
        return Map.of("clientSecret", intent.getClientSecret());
    }
    
    

}
