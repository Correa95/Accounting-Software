package com.project.backend.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Wraps incoming requests in a ContentCachingRequestWrapper so that
 * the raw request body can be read multiple times.
 *
 * WHY THIS IS CRITICAL:
 * Stripe's Webhook.constructEvent() verifies the payload signature using
 * the raw byte-for-byte request body. Spring's HttpServletRequest body
 * can only be read once by default — if any filter or interceptor reads
 * it first (e.g. for logging), the body is consumed and the webhook
 * controller receives an empty string, causing signature verification to fail.
 *
 * This filter ensures the body remains readable for both logging
 * and the webhook endpoint.
 *
 * Apply only to the webhook path to avoid unnecessary overhead on
 * other endpoints — configure this in WebConfig or use @Order.
 */
@Component
public class StripeWebhookFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) 
                    throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Only wrap webhook requests — all others pass through untouched
        if (httpRequest.getRequestURI().contains("/api/payments/webhook")) {
            ContentCachingRequestWrapper wrappedRequest =
                    new ContentCachingRequestWrapper(httpRequest);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}