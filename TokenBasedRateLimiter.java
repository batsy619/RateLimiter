package ratelimiter;

public class TokenBasedRateLimiter implements RateLimiter{

    @Override
    public boolean allow(String id) {
        return false;
    }
}
