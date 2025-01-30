package com.notification.redis_notification.service;

import com.notification.redis_notification.model.Notification;
import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);
    void processNotification(String message);
    List<Notification> getAllNotifications();
}
