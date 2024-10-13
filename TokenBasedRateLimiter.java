package ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBasedRateLimiter implements RateLimiter {

    int tokenCount;
    int maxTokenCount;
    long refillInMs;
    int refillTokens;
    long lastRefillTime;

    public TokenBasedRateLimiter(int maxTokenCount,  long refillInMs, int refillTokens) {
        this.maxTokenCount = maxTokenCount;
        this.refillInMs = refillInMs;
        this.refillTokens = refillTokens;
        tokenCount = maxTokenCount;
        lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allow(String requestId) {
        refill();
        if (tokenCount > 0) {
            tokenCount--;
            return true;
        }
        return false;
    }

    private void refill() {
        long currentTime = System.currentTimeMillis();
        int numOfRefills = (int) ((currentTime - lastRefillTime)/refillInMs);
        if (numOfRefills > 0) {
            tokenCount = Math.min(maxTokenCount, numOfRefills* refillTokens);
            lastRefillTime = currentTime;
        }
    }
}
