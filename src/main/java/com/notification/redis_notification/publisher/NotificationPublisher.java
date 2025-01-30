package com.notification.redis_notification.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import com.notification.redis_notification.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class NotificationPublisher {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ChannelTopic topic;
    
    @Autowired
    private ObjectMapper objectMapper;

    public void publish(Notification notification) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            
            String key = "notification:" + notification.getId();
            redisTemplate.opsForValue().set(key, jsonMessage);
            
            redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
            
            System.out.println("Stored in Redis with key: " + key);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish notification: " + e.getMessage());
        }
    }
}
