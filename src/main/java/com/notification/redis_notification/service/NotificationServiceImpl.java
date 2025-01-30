package com.notification.redis_notification.service;

import com.notification.redis_notification.service.NotificationService;
import com.notification.redis_notification.dao.NotificationDao;
import com.notification.redis_notification.publisher.NotificationPublisher;
import com.notification.redis_notification.model.Notification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationPublisher publisher;
    
    @Autowired
    private NotificationDao notificationDao;
    
    private static long idCounter = 1;

    @Override
    public void sendNotification(Notification notification) {
        try {
            notification.setId(idCounter++);  // Set ID
            notification.setTimestamp(LocalDateTime.now());
            publisher.publish(notification);
            notificationDao.saveNotification(notification);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    public void processNotification(String message) {
        try {
            Notification notification = new Notification();
            notification.setMessage(message);
            notificationDao.saveNotification(notification);
        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
        }
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }
}