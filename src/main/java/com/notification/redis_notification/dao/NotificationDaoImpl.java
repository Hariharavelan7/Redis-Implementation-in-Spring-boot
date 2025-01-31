package com.notification.redis_notification.dao;

import com.notification.redis_notification.model.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationDaoImpl implements NotificationDao {
    
	@Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_NOTIFICATION = 
        "INSERT INTO notifications (message, recipient, timestamp, status) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_ALL = 
        "SELECT * FROM notifications ORDER BY timestamp DESC";
    
    private static final String SELECT_BY_RECIPIENT = 
        "SELECT * FROM notifications WHERE recipient = ? ORDER BY timestamp DESC";

    @Override
    public void saveNotification(Notification notification) {
        jdbcTemplate.update(INSERT_NOTIFICATION,
            notification.getMessage(),
            notification.getRecipient(),
            notification.getTimestamp(),
            notification.getStatus()
        );
    }

    @Override
    public List<Notification> getAllNotifications() {
        return jdbcTemplate.query(SELECT_ALL, new NotificationRowMapper());
    }

    @Override
    public List<Notification> getNotificationsForUser(String recipient) {
        return jdbcTemplate.query(SELECT_BY_RECIPIENT, new NotificationRowMapper(), recipient);
    }

    private static class NotificationRowMapper implements RowMapper<Notification> {
        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notification = new Notification();
            notification.setId(rs.getLong("id"));
            notification.setMessage(rs.getString("message"));
            notification.setRecipient(rs.getString("recipient"));
            notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            notification.setStatus(rs.getString("status"));
            return notification;
        }
    }
}
