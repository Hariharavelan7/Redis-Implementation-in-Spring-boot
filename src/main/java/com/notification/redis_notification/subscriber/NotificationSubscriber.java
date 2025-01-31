package com.notification.redis_notification.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.notification.redis_notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.redis_notification.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NotificationSubscriber implements MessageListener {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationSubscriber.class);

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String receivedMessage = new String(message.getBody());
            log.info("Received notification: {}", receivedMessage);
            
            // Convert JSON to Notification object
            Notification notification = objectMapper.readValue(
                receivedMessage, Notification.class);
            
            // Process notification
            notificationService.processNotification(notification);
            
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
