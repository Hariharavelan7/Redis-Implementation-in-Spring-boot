package com.notification.redis_notification.dao;

import static org.jooq.impl.DSL.*;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.notification.redis_notification.model.Notification;
import java.util.List;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @Autowired
    private DSLContext dsl;

    @Override
    public void saveNotification(Notification notification) {
        dsl.insertInto(table("notifications"))
           .columns(field("message"),field("recipient"), field("timestamp"),
               field("status")
           ).values(notification.getMessage(), notification.getRecipient(), notification.getTimestamp(),
               notification.getStatus())
           .execute();
    }

    @Override
    public List<Notification> getAllNotifications() {
        Result<Record> result = dsl.select().from(table("notifications")).orderBy(field("timestamp").desc()).fetch();

        return result.map(this::mapToNotification);
    }

    @Override
    public List<Notification> getNotificationsForUser(String recipient) {
        Result<Record> result = dsl.select().from(table("notifications")).where(field("recipient").eq(recipient)).orderBy(field("timestamp").desc())
            .fetch();

        return result.map(this::mapToNotification);
    }

    private Notification mapToNotification(Record record) {
        Notification notification = new Notification();
        notification.setId(record.get("id", Long.class));
        notification.setMessage(record.get("message", String.class));
        notification.setRecipient(record.get("recipient", String.class));
        notification.setTimestamp(record.get("timestamp", java.time.LocalDateTime.class));
        notification.setStatus(record.get("status", String.class));
        return notification;
    }
}
