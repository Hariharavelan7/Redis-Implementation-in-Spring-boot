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

    private static final String Inserting = "insert into notifications (message, recipient, timestamp) Values (?, ?, Current_timestamp)";
    
    private static final String Select_all = "select id, message, recipient, timestamp From notifications Order By timestamp Desc";

    @Override
    public void saveNotification(Notification notification) {
        jdbcTemplate.update(Inserting, notification.getMessage(), notification.getRecipient());
    }

    @Override
    public List<Notification> getAllNotifications() {
        return jdbcTemplate.query(Select_all, new NotificationRowMapper());
    }

    private static class NotificationRowMapper implements RowMapper<Notification> {
        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notification = new Notification();
            notification.setId(rs.getLong("id"));
            notification.setMessage(rs.getString("message"));
            notification.setRecipient(rs.getString("recipient"));
            notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return notification;
        }
    }
}
