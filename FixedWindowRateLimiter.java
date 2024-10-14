package ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FixedWindowRateLimiter implements RateLimiter{

    private final long windowLengthInms;
    private final int maxCountAllowed;
    private final AtomicLong timeInMs;
    private AtomicInteger currentCount;

    public FixedWindowRateLimiter(long windowLengthInms, int maxCountAllowed) {
        this.windowLengthInms = windowLengthInms;
        this.maxCountAllowed = maxCountAllowed;
        this.currentCount = new AtomicInteger(maxCountAllowed);
        this.timeInMs = new AtomicLong(System.currentTimeMillis());

    }

    @Override
    public boolean allow(String id) {
        long currentTime = System.currentTimeMillis();
        if (timeInMs.get() + windowLengthInms < currentTime) {
            timeInMs.set(currentTime);
            currentCount.set(maxCountAllowed);
        }
        if (currentCount.get() > 0) {
            currentCount.decrementAndGet();
            return true;
        }
        return false;
    }
}
