package com.notification.redis_notification.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RedisHealthIndicator extends AbstractHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(RedisHealthIndicator.class);
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String pong = new String(connection.ping());
            if ("PONG".equals(pong)) {
                log.info("Redis health check: UP");
                builder.up()
                    .withDetail("ping", "PONG")
                    .withDetail("version", connection.info().getProperty("redis_version"));
            } else {
                log.warn("Redis health check: DOWN (no PONG response)");
                builder.down().withDetail("ping", "no response");
            }
        } catch (Exception e) {
            log.error("Redis health check: DOWN ({})", e.getMessage());
            builder.down(e);
        }
    }
} 