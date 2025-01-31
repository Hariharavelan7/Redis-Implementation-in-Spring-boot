package com.notification.redis_notification.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import com.notification.redis_notification.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChannelTopic topic;

    @Autowired
    private ObjectMapper objectMapper;

    public void publish(Notification notification) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            
            // Store in Redis
            String key = "notification:" + notification.getId();
            redisTemplate.opsForValue().set(key, jsonMessage, 1, TimeUnit.HOURS);
            
            // Publish to channel
            redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
            
            log.info("Published notification: {} to channel: {}", 
                    notification.getId(), topic.getTopic());
            
        } catch (Exception e) {
            log.error("Failed to publish notification: {}", e.getMessage());
            throw new RuntimeException("Failed to publish notification: " + e.getMessage());
        }
    }
}
