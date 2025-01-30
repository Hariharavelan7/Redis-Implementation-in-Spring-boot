package com.notification.redis_notification.dao;

import com.notification.redis_notification.model.Notification;
import java.util.List;

public interface NotificationDao {
    void saveNotification(Notification notification);
    List<Notification> getAllNotifications();
}
