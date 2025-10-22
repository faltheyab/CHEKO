package com.faisal.cheko.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class RateLimiterService {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);
    private static final int DEFAULT_CAPACITY = 20;  // Maximum number of tokens
    private static final int DEFAULT_REFILL_TOKENS = 1;  // Tokens to add per refill
    private static final Duration DEFAULT_REFILL_PERIOD = Duration.ofSeconds(1);  // Refill period
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public boolean allowRequest(String clientIp) {
        // Get or create a token bucket for this client
        TokenBucket bucket = buckets.computeIfAbsent(clientIp, 
                ip -> new TokenBucket(DEFAULT_CAPACITY, DEFAULT_REFILL_TOKENS, DEFAULT_REFILL_PERIOD));
        
        // Try to consume a token
        boolean allowed = bucket.tryConsume();
        
        if (!allowed) {
            log.warn("Rate limit exceeded for client IP: {}", clientIp);
        }
        
        return allowed;
    }

    public boolean allowRequest(String clientIp, int tokens) {
        TokenBucket bucket = buckets.computeIfAbsent(clientIp,
                ip -> new TokenBucket(DEFAULT_CAPACITY, DEFAULT_REFILL_TOKENS, DEFAULT_REFILL_PERIOD));
        
        boolean allowed = bucket.tryConsume(tokens);
        
        if (!allowed) {
            log.warn("Rate limit exceeded for client IP: {}", clientIp);
        }
        
        return allowed;
    }

    public int getRemainingTokens(String clientIp) {
        TokenBucket bucket = buckets.get(clientIp);
        return bucket != null ? bucket.getTokens() : DEFAULT_CAPACITY;
    }


    private static class TokenBucket {
        private final int capacity;
        private final int refillTokens;
        private final long refillPeriodNanos;
        private int tokens;
        private long lastRefillTimestamp;

        public TokenBucket(int capacity, int refillTokens, Duration refillPeriod) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillPeriodNanos = refillPeriod.toNanos();
            this.tokens = capacity;
            this.lastRefillTimestamp = System.nanoTime();
        }

        public synchronized boolean tryConsume() {
            return tryConsume(1);
        }
        public synchronized boolean tryConsume(int tokensToConsume) {
            refill();
            
            if (tokens >= tokensToConsume) {
                tokens -= tokensToConsume;
                return true;
            }
            
            return false;
        }
        public synchronized int getTokens() {
            refill();
            return tokens;
        }

        private void refill() {
            long now = System.nanoTime();
            long elapsedNanos = now - lastRefillTimestamp;
            long refillCount = elapsedNanos / refillPeriodNanos;
            if (refillCount > 0) {
                int tokensToAdd = (int) (refillCount * refillTokens);
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTimestamp += refillCount * refillPeriodNanos;
            }
        }
    }
}
