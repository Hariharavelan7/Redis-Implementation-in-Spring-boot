package com.notification.redis_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class RedisNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisNotificationApplication.class, args);
    }
}
