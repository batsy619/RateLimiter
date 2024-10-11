package ratelimiter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class SlidingLogWindowRateLimiterTest {

    @Test
    void testAllInLimit() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        RateLimiter rateLimiterSliding = new SlidingLogWindowRateLimiter(1000, 5);
        List<Boolean> results = new ArrayList<>();
        for(int i=0; i<5; i++) {
            executorService.submit(() -> {
                try {
                    results.add(rateLimiterSliding.allow("id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));
        assertIterableEquals(List.of(true, true, true, true, true), results);
    }

    @Test
    void testThrottled() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        RateLimiter rateLimiterSliding = new SlidingLogWindowRateLimiter(100000, 5);
        List<Boolean> results = new ArrayList<>();
        for(int i=0; i<10; i++) {

            executorService.submit(() -> {
                try {
                    results.add(rateLimiterSliding.allow("id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));
        assertIterableEquals(List.of(true, true, true, true, true, false, false, false, false, false), results);
    }
}