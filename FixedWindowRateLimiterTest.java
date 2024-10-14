package ratelimiter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FixedWindowRateLimiterTest {

    @Test
    public void testAllRequestWithInLimit() {
        RateLimiter limiter = new FixedWindowRateLimiter(1000, 5);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i<5;i++) {
            results.add(limiter.allow("id"));
        }
        assertIterableEquals(results, List.of(true, true, true, true, true));
    }

    @Test
    public void testFewRequestThrottled() {
        RateLimiter limiter = new FixedWindowRateLimiter(1000, 5);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i<7;i++) {
            results.add(limiter.allow("id"));
        }
        assertIterableEquals(results, List.of(true, true, true, true, true, false, false));
    }

    @Test
    public void testFewRequestComingAfterWindowLimit() throws InterruptedException {
        RateLimiter limiter = new FixedWindowRateLimiter(500, 5);
        List<Boolean> results = new ArrayList<>();
        for (int i=0;i<6;i++) {
            results.add(limiter.allow("id"));
        }
        Thread.sleep(1600);
        for (int i=0;i<6;i++) {
            results.add(limiter.allow("id"));
        }
        System.out.println(results);
        assertIterableEquals(results, List.of(true, true, true, true, true, false, true, true, true, true, true, false));
    }
}