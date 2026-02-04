// package com.project.backend.service;

// import java.math.BigDecimal;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import com.project.backend.dto.PaymentRequest;
// import com.project.backend.dto.PaymentResponse;
// import com.project.backend.enums.PaymentStatus;
// import com.project.backend.service.JournalEntryService;
// import com.project.backend.service.StripeService;
// import com.stripe.Stripe;
// import com.stripe.model.Event;
// import com.stripe.model.PaymentIntent;
// import com.stripe.net.Webhook;
// import com.stripe.param.PaymentIntentCreateParams;

// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class StripeServiceImpl implements StripeService {
//     private final JournalEntryService journalEntryService;
//     @Override
//     public PaymentResponse createPayment(PaymentRequest paymentRequest) {

//         PaymentIntentCreateParams params =PaymentIntentCreateParams
//             .builder()
//             .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
//             .setCurrency(paymentRequest.getCurrency())
//             .setReceiptEmail(paymentRequest.getCustomerEmail())
//             .build();

//         try {
//             PaymentIntent paymentIntent = PaymentIntent.create(params);

//             return PaymentResponse.builder()
//                 .paymentIntentId(paymentIntent.getId())
//                 .clientSecret(paymentIntent.getClientSecret())
//                 .amount(paymentRequest.getAmount())
//                 .currency(paymentRequest.getCurrency())
//                 .paymentStatus(PaymentStatus.PENDING)
//                 .message("Payment intent created")
//                 .build();

//         } catch (Exception e) {
//             throw new RuntimeException("Stripe payment failed", e);
//         }
//     }

//     @Override
//     public void handleWebhook(String payload, String sigHeader) {
//         try {
//             Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

//             if ("payment_intent.succeeded".equals(event.getType())) {
//                 PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
//                         .getObject().orElseThrow();

//                 // 🔑 Accounting happens here
//                 journalEntryService.recordStripePayment(paymentIntent);
//             }

//         } catch (Exception e) {
//             throw new RuntimeException("Webhook processing failed", e);
//         }
//     }
// }
