package com.notification.redis_notification.service;

import com.notification.redis_notification.service.NotificationService;
import com.notification.redis_notification.dao.NotificationDao;
import com.notification.redis_notification.publisher.NotificationPublisher;
import com.notification.redis_notification.model.Notification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationPublisher publisher;

    @Autowired
    private NotificationDao notificationDao;

    private static AtomicLong idCounter = new AtomicLong(1);

    @Override
    public void sendNotification(Notification notification) {
        try {
            notification.setId(idCounter.getAndIncrement());
            notification.setTimestamp(LocalDateTime.now());
            notification.setStatus("SENT");

            // Publish to Redis
            publisher.publish(notification);

            // Save to PostgreSQL
            notificationDao.saveNotification(notification);

            log.info("Notification sent successfully: {}", notification.getId());
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            throw new RuntimeException("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    public void processNotification(Notification notification) {
        try {
            notification.setStatus("DELIVERED");
            notificationDao.saveNotification(notification);
            log.info("Notification processed: {}", notification.getId());
        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage());
        }
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    @Override
    public List<Notification> getNotificationsForUser(String recipient) {
        return notificationDao.getNotificationsForUser(recipient);
    }
}
