package com.example.dao;

import com.example.util.DBConnection;

import java.sql.*;
import java.util.*;

public class SubmissionDAO {

    // =========================
    // STUDENT: submit homework
    // =========================
    public void insertSubmission(int homeworkId, int studentId, String filePath, String status) {

        String sql = """
            INSERT INTO submissions (homework_id, student_id, file_path, status)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                file_path = VALUES(file_path),
                status = VALUES(status),
                submit_time = CURRENT_TIMESTAMP
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ps.setInt(2, studentId);
            ps.setString(3, filePath);
            ps.setString(4, status);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // TEACHER: grade submission
    // =========================
    public void gradeSubmission(int submissionId, int score, String feedback) {

    String sql = """
        UPDATE submissions
        SET score = ?, feedback = ?, status = 'graded'
        WHERE submission_id = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, score);
        ps.setString(2, feedback);
        ps.setInt(3, submissionId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // =========================
    // STUDENT: view own submissions
    // =========================
    public List<Map<String, Object>> getSubmissionsByStudent(int studentId) {

        String sql = """
            SELECT s.submission_id,
                   h.title,
                   s.submit_time,
                   s.status,
                   s.score,
                   s.feedback
            FROM submissions s
            JOIN homework h ON s.homework_id = h.homework_id
            WHERE s.student_id = ?
            ORDER BY s.submit_time DESC
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int homeworkId = rs.getInt("homework_id");

                row.put("submissionId", rs.getInt("submission_id"));
                row.put("homeworkTitle", rs.getString("title"));
                row.put("submitTime", rs.getTimestamp("submit_time"));
                row.put("status", rs.getString("status"));
                row.put("score", rs.getObject("score"));
                row.put("feedback", rs.getString("feedback"));

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TEACHER: students who submitted
    // =========================
    public List<Map<String, Object>> getSubmittedStudents(int homeworkId) {

        String sql = """
            SELECT u.user_id, u.username, u.name,
                   s.submit_time, s.status, s.score
            FROM submissions s
            JOIN users u ON s.student_id = u.user_id
            WHERE s.homework_id = ?
            ORDER BY s.submit_time DESC
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("studentId", rs.getInt("user_id"));
                row.put("username", rs.getString("username"));
                row.put("name", rs.getString("name"));
                row.put("submitTime", rs.getTimestamp("submit_time"));
                row.put("status", rs.getString("status"));
                row.put("score", rs.getObject("score"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TEACHER: overdue students
    // =========================
    public List<Map<String, Object>> getOverdueStudents(int homeworkId) {

        String sql = """
            SELECT u.user_id, u.username, u.name
            FROM users u
            JOIN courses_users cu ON u.user_id = cu.user_id
            JOIN homework h ON cu.course_id = h.course_id
            LEFT JOIN submissions s
                ON s.student_id = u.user_id
               AND s.homework_id = h.homework_id
            WHERE h.homework_id = ?
              AND u.role = 'student'
              AND u.status = 'active'
              AND h.deadline < NOW()
              AND s.submission_id IS NULL
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("studentId", rs.getInt("user_id"));
                row.put("username", rs.getString("username"));
                row.put("name", rs.getString("name"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TEACHER: ALL students (submitted + not submitted)
    // =========================
    public List<Map<String, Object>> getAllStudentsSubmissionStatus(int homeworkId) {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT u.user_id, u.name,
                   s.submission_id,
                   s.submit_time,
                   s.status,
                   s.score
            FROM courses_users cu
            JOIN users u ON cu.user_id = u.user_id
            JOIN homework h ON cu.course_id = h.course_id
            LEFT JOIN submissions s
                ON s.student_id = u.user_id
               AND s.homework_id = ?
            WHERE h.homework_id = ?
            ORDER BY u.name
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeworkId);
            ps.setInt(2, homeworkId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("studentName", rs.getString("name"));
                row.put("submissionId", rs.getObject("submission_id"));
                row.put("submitTime", rs.getTimestamp("submit_time"));
                row.put("status", rs.getString("status"));
                row.put("score", rs.getObject("score"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // STUDENT: get submission by student & homework
    // =========================
    public Map<String, Object> getSubmissionByStudentAndHomework(
            int studentId,
            int homeworkId
    ) {

    String sql = """
        SELECT submission_id, status, submit_time, score, file_path, feedback
        FROM submissions
        WHERE student_id = ?
          AND homework_id = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, homeworkId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("submissionId", rs.getInt("submission_id"));
            row.put("status", rs.getString("status"));
            row.put("submitTime", rs.getTimestamp("submit_time"));
            row.put("score", rs.getObject("score"));
            row.put("filePath", rs.getString("file_path"));
            row.put("feedback", rs.getString("feedback"));
            return row;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
    }



}
