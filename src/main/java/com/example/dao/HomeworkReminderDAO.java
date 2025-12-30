package com.example.dao;

import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeworkReminderDAO {

    // CREATE / UPDATE reminder
    public void saveReminder(int homeworkId, int studentId, LocalDateTime remindAt) {

        String sql = """
            INSERT INTO homework_reminders (homework_id, student_id, remind_at)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE remind_at = VALUES(remind_at)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ps.setInt(2, studentId);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(remindAt));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET upcoming reminders (student dashboard)
    public List<String> getUpcomingReminders(int studentId) {

        String sql = """
            SELECT h.title, r.remind_at
            FROM homework_reminders r
            JOIN homework h ON r.homework_id = h.homework_id
            WHERE r.student_id = ?
              AND r.remind_at >= NOW()
            ORDER BY r.remind_at ASC
        """;

        List<String> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                    rs.getString("title") +
                    " → " +
                    rs.getTimestamp("remind_at").toLocalDateTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
