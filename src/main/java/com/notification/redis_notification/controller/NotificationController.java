package com.notification.redis_notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.notification.redis_notification.model.ApiResponse;
import com.notification.redis_notification.model.Notification;
import com.notification.redis_notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Notification notification) {
        try {
            System.out.println("Received notification request: " + notification);
            notificationService.sendNotification(notification);
            return ResponseEntity.ok()
                .body(new ApiResponse("Notification sent successfully", true));
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e);
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
            System.out.println("Error fetching notifications: " + e);
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to fetch notifications: " + e.getMessage(), false));
        }
    }
}