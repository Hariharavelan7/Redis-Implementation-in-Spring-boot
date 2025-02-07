package com.notification.redis_notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.notification.redis_notification.model.ApiResponse;
import com.notification.redis_notification.model.Notification;
import com.notification.redis_notification.service.NotificationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Notification notification) {
        try {
            logger.info("Received notification request: {}", notification);
            notificationService.sendNotification(notification);
            return ResponseEntity.ok()
                .body(new ApiResponse("Notification sent successfully", true));
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to send notification: " + e.getMessage(), false));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifications() {
        try {
            List<Notification> notifications = notificationService.getAllNotifications();
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e) {
            logger.error("Error fetching notifications: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to fetch notifications: " + e.getMessage(), false));
        }
    }
    
    @GetMapping("/user/{recipient}")
    public ResponseEntity<?> getNotificationsForUser(@PathVariable String recipient) {
        try {
            logger.info("Fetching notifications for user: {}", recipient);
            List<Notification> notifications = 
                notificationService.getNotificationsForUser(recipient);
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e) {
            logger.error("Error fetching notifications for user: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to fetch notifications: " + e.getMessage(), false));
        }
    }
    
}