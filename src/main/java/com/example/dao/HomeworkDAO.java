package com.example.dao;

import com.example.model.Homework;
import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeworkDAO {

    // =========================
    // STUDENT: view all homework
    // =========================
    public List<Homework> getAllHomework() {

        List<Homework> list = new ArrayList<>();

        String sql = """
            SELECT homework_id, course_id, teacher_id,
                   title, description, difficulty,
                   deadline, allowed_format
            FROM homework
            ORDER BY deadline ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Homework hw = new Homework();
                hw.setHomeworkId(rs.getInt("homework_id"));
                hw.setCourseId(rs.getInt("course_id"));
                hw.setTeacherId(rs.getInt("teacher_id"));
                hw.setTitle(rs.getString("title"));
                hw.setDescription(rs.getString("description"));
                hw.setDifficulty(rs.getString("difficulty"));
                hw.setDeadline(rs.getTimestamp("deadline").toLocalDateTime());
                hw.setAllowedFormat(rs.getString("allowed_format"));
                list.add(hw);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // DEADLINE CHECK
    // =========================
    public Timestamp getDeadlineByHomeworkId(int homeworkId) {

        String sql = "SELECT deadline FROM homework WHERE homework_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getTimestamp("deadline");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // TEACHER: view own homework
    // =========================
    public List<Map<String, Object>> getHomeworkByTeacher(int teacherId) {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT homework_id, title, difficulty, deadline, allowed_format
            FROM homework
            WHERE teacher_id = ?
            ORDER BY deadline DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("homeworkId", rs.getInt("homework_id"));
                row.put("title", rs.getString("title"));
                row.put("difficulty", rs.getString("difficulty"));
                row.put("deadline", rs.getTimestamp("deadline"));
                row.put("format", rs.getString("allowed_format"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TEACHER: create homework
    // =========================
    public void createHomework(int teacherId,
                            int courseId,
                            String title,
                            String desc,
                            String difficulty,
                            Timestamp deadline,
                            String format) {

        String sql = """
            INSERT INTO homework
                (course_id, teacher_id, title, description, difficulty, deadline, allowed_format)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, teacherId);
            ps.setString(3, title);
            ps.setString(4, desc);
            ps.setString(5, difficulty);
            ps.setTimestamp(6, deadline);
            ps.setString(7, format);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // kalau gagal, pasti kelihatan sekarang
        }
    }

    // =========================
    // TEACHER: get homework by id
    // =========================
    public Map<String, Object> getHomeworkById(int homeworkId) {

        String sql = """
            SELECT homework_id, title, description,
                   difficulty, deadline, allowed_format
            FROM homework
            WHERE homework_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> hw = new HashMap<>();
                hw.put("homeworkId", rs.getInt("homework_id"));
                hw.put("title", rs.getString("title"));
                hw.put("description", rs.getString("description"));
                hw.put("difficulty", rs.getString("difficulty"));
                hw.put("deadline", rs.getTimestamp("deadline"));
                hw.put("format", rs.getString("allowed_format"));
                return hw;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // TEACHER: update homework
    // =========================
    public void updateHomework(int homeworkId,
                               String title,
                               String desc,
                               String difficulty,
                               Timestamp deadline,
                               String format) {

        String sql = """
            UPDATE homework
            SET title = ?, description = ?, difficulty = ?,
                deadline = ?, allowed_format = ?
            WHERE homework_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, difficulty);
            ps.setTimestamp(4, deadline);
            ps.setString(5, format);
            ps.setInt(6, homeworkId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // TEACHER: delete homework
    // =========================
    public void deleteHomework(int homeworkId) {
        String deleteRemindersSql = "DELETE FROM homework_reminders WHERE homework_id = ?";
        String deleteSubmissionsSql = "DELETE FROM submissions WHERE homework_id = ?";
        String deleteHomeworkSql = "DELETE FROM homework WHERE homework_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Delete reminders
            try (PreparedStatement psReminders = conn.prepareStatement(deleteRemindersSql)) {
                psReminders.setInt(1, homeworkId);
                psReminders.executeUpdate();
            }

            // 2. Delete submissions
            try (PreparedStatement psSubmissions = conn.prepareStatement(deleteSubmissionsSql)) {
                psSubmissions.setInt(1, homeworkId);
                psSubmissions.executeUpdate();
            }

            // 3. Delete homework
            try (PreparedStatement psHomework = conn.prepareStatement(deleteHomeworkSql)) {
                psHomework.setInt(1, homeworkId);
                psHomework.executeUpdate();
            }

            // If all deletions were successful, commit the transaction
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            // If any error occurred, roll back the transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // End transaction and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // =========================
    // TEACHER: homework by course
    // =========================
    public List<Map<String, Object>> getHomeworkByCourse(int courseId) {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT homework_id, title, deadline
            FROM homework
            WHERE course_id = ?
            ORDER BY deadline ASC
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("homeworkId", rs.getInt("homework_id"));
                row.put("title", rs.getString("title"));
                row.put("deadline", rs.getTimestamp("deadline"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TEACHER: create homework + return ID
    // =========================
    public int createHomeworkReturnId(
            int teacherId,
            int courseId,
            String title,
            String desc,
            String difficulty,
            Timestamp deadline,
            String format
    ) {

        String sql = """
            INSERT INTO homework
                (course_id, teacher_id, title, description, difficulty, deadline, allowed_format)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps =
                conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, courseId);
            ps.setInt(2, teacherId);
            ps.setString(3, title);
            ps.setString(4, desc);
            ps.setString(5, difficulty);
            ps.setTimestamp(6, deadline);
            ps.setString(7, format);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    

}
