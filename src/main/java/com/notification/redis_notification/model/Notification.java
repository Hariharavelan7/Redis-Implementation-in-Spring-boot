package com.notification.redis_notification.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notification {
    private Long id;
    private String message;
    private String recipient;
    private LocalDateTime timestamp;
    private String status = "SENT"; 

    // Default constructor
    public Notification() {
    }

    // All-args constructor
    public Notification(Long id, String message, String recipient, LocalDateTime timestamp, String status) {
        this.id = id;
        this.message = message;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", recipient='" + recipient + '\'' +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(message, that.message) &&
                Objects.equals(recipient, that.recipient) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(status, that.status);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, message, recipient, timestamp, status);
    }
}
