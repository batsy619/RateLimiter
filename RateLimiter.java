package ratelimiter;

public interface RateLimiter {

    boolean allow(String id);
}
