package com.notification.redis_notification.failover;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RedisFailoverHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisFailoverHandler.class);
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisFailoverHandler(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Retryable(
        value = RedisConnectionFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public boolean executeWithFailover(RedisOperation operation) {
        try {
            operation.execute(redisConnectionFactory.getConnection());
            return true;
        } catch (Exception e) {
            log.error("Redis operation failed: {}", e.getMessage());
            throw new RedisConnectionFailureException("Failed to execute Redis operation", e);
        }
    }

    @Recover
    public boolean fallback(RedisConnectionFailureException e) {
        log.error("All retry attempts failed. Using fallback mechanism");
        return false;
    }

    @FunctionalInterface
    public interface RedisOperation {
        void execute(org.springframework.data.redis.connection.RedisConnection connection);
    }
} 