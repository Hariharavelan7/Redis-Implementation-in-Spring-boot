package com.notification.redis_notification.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import com.notification.redis_notification.model.Notification;
import com.notification.redis_notification.failover.RedisFailoverHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;
    private final RedisFailoverHandler failoverHandler;

    public NotificationPublisher(
            RedisTemplate<String, Object> redisTemplate,
            ChannelTopic topic,
            ObjectMapper objectMapper,
            RedisFailoverHandler failoverHandler) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.objectMapper = objectMapper;
        this.failoverHandler = failoverHandler;
    }

    public void publish(Notification notification) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            
            boolean published = failoverHandler.executeWithFailover(connection -> {
                String key = "notification:" + notification.getId();
                redisTemplate.opsForValue().set(key, jsonMessage, 1, TimeUnit.HOURS);
                
                connection.publish(
                    topic.getTopic().getBytes(), 
                    jsonMessage.getBytes()
                );
                
                log.info("Published notification: {} to channel: {}", 
                        notification.getId(), topic.getTopic());
            });

            if (!published) {
                log.warn("Failed to publish notification to Redis, using fallback");
            }
            
        } catch (Exception e) {
            log.error("Failed to publish notification: {}", e.getMessage());
            throw new RuntimeException("Failed to publish notification: " + e.getMessage());
        }
    }
}
