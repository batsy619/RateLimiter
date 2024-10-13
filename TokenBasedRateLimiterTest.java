package ratelimiter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

class TokenBasedRateLimiterTest {

    @Test
    public void testAllAllowedRequest() {
        RateLimiter rateLimiter = new TokenBasedRateLimiter(5, 1000, 1);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i< 5;i++) {
            results.add(rateLimiter.allow("id"));
        }
        assertIterableEquals(results, List.of(true, true, true, true, true));
    }

    @Test
    public void testAllNotAllowedRequest() {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        RateLimiter rateLimiter = new TokenBasedRateLimiter(0, 1000, 0);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i< 5;i++) {
            results.add(rateLimiter.allow("id"));
        }
        assertIterableEquals(results, List.of(false, false, false, false, false));
    }

    @Test
    public void testRefillAllowedRequest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        RateLimiter rateLimiter = new TokenBasedRateLimiter(2, 1000, 1);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i< 5;i++) {
            results.add(rateLimiter.allow("id"));
        }
        Thread.sleep(1000);
        for (int i=0;i< 5;i++) {
            results.add(rateLimiter.allow("id"));
        }
        Thread.sleep(3000);
        for (int i=0;i< 5;i++) {
            results.add(rateLimiter.allow("id"));
        }
        assertIterableEquals(results, List.of(true, true, false, false, false, true, false, false, false, false, true, true, false, false, false));
    }

}