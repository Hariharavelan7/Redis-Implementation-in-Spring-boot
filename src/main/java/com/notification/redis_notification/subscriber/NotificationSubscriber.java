package com.notification.redis_notification.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NotificationSubscriber implements MessageListener {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String receivedMessage = new String(message.getBody());
            log.info("Received notification: {}", receivedMessage);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
