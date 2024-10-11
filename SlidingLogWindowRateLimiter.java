package ratelimiter;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingLogWindowRateLimiter implements RateLimiter {

    private final long windowLimitInMS;
    private final int maxRequests;
    ConcurrentLinkedQueue<Long> limitTracker;


    public SlidingLogWindowRateLimiter(long windowLimitInMS, int maxRequests) {
        this.maxRequests = maxRequests;
        this.windowLimitInMS = windowLimitInMS;
        this.limitTracker = new ConcurrentLinkedQueue<>();
    }
    @Override
    public boolean allow(String id) {
        long currentTime = System.currentTimeMillis();
        refresh(currentTime);
        if (limitTracker.size() >= maxRequests) {
           return false;
        }
        limitTracker.add(currentTime);
        return true;
    }

    private void refresh(long currentTime) {
        while (!limitTracker.isEmpty() && limitTracker.peek() < currentTime - windowLimitInMS) {
            System.out.println("polled"+ limitTracker.poll());
            limitTracker.poll();
        }
    }
}
