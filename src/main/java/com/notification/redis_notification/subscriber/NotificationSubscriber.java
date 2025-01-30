package com.notification.redis_notification.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.notification.redis_notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationSubscriber implements MessageListener {
    
    private NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receivedMessage = new String(message.getBody());
        System.out.println("Received notification: " + receivedMessage);
        notificationService.processNotification(receivedMessage);
    }
}
