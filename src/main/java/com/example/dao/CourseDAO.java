package com.example.dao;

import com.example.util.DBConnection;
import java.sql.*;
import java.util.*;

public class CourseDAO {

    // =========================
    // GET ALL COURSES
    // =========================
    public List<Map<String, Object>> getAllCourses() {

    List<Map<String, Object>> list = new ArrayList<>();

    String sql = """
        SELECT
            c.course_id,
            c.course_name,
            t.name AS teacher_name,
            COUNT(cu.user_id) AS student_count
        FROM courses c
        LEFT JOIN users t
            ON c.teacher_id = t.user_id
        LEFT JOIN courses_users cu
            ON c.course_id = cu.course_id
        GROUP BY c.course_id, c.course_name, t.name
        ORDER BY c.course_name
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("courseId", rs.getInt("course_id"));
            row.put("courseName", rs.getString("course_name"));
            row.put("teacher", rs.getString("teacher_name")); 
            row.put("students", rs.getInt("student_count")); 
            list.add(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


    // =========================
    // GET ALL TEACHERS
    // =========================
    public List<Map<String, Object>> getAllTeachers() {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT user_id, name
            FROM users
            WHERE role = 'teacher'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("teacherId", rs.getInt("user_id"));
                row.put("teacherName", rs.getString("name"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // ASSIGN TEACHER TO COURSE
    // =========================
    public void assignTeacher(int courseId, int teacherId) {

        String sql = """
            UPDATE courses
            SET teacher_id = ?
            WHERE course_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.setInt(2, courseId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CREATE COURSE (NO TEACHER)
    // =========================
    public void createCourse(String courseName) {

        String sql = """
            INSERT INTO courses (course_name)
            VALUES (?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseName);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // GET ALL STUDENTS
    // =========================
    public List<Map<String,Object>> getAllStudents() {

    List<Map<String,Object>> list = new ArrayList<>();

    String sql = """
        SELECT user_id, username, name
        FROM users
        WHERE role = 'student'
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String,Object> row = new HashMap<>();
            row.put("studentId", rs.getInt("user_id"));

            // FALLBACK PALING PENTING
            row.put("studentName",
                rs.getString("name") != null
                    ? rs.getString("name")
                    : rs.getString("username")
            );

            list.add(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // =========================
    // ASSIGN STUDENT TO COURSE
    // =========================
    public void assignStudent(int courseId, int studentId) {

        String sql = """
            INSERT INTO courses_users (course_id, user_id)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE course_id = course_id
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // GET COURSES BY TEACHER
    // =========================
    public List<Map<String,Object>> getCoursesByTeacher(int teacherId) {
    List<Map<String,Object>> list = new ArrayList<>();

    String sql = """
        SELECT c.course_id, c.course_name
        FROM courses c
        WHERE c.teacher_id = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, teacherId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String,Object> row = new HashMap<>();
            row.put("courseId", rs.getInt("course_id"));
            row.put("courseName", rs.getString("course_name"));
            list.add(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
    }

    // =========================
    // GET COURSES OVERVIEW BY TEACHER
    // ========================= 
    public List<Map<String,Object>> getCoursesOverviewByTeacher(int teacherId) {

    List<Map<String,Object>> list = new ArrayList<>();

    String sql = """
        SELECT
            c.course_id,
            c.course_name,

            COUNT(DISTINCT h.homework_id) AS total_homework,

            SUM(
                CASE
                    WHEN s.submission_id IS NULL
                     AND h.deadline < NOW()
                    THEN 1 ELSE 0
                END
            ) AS overdue_count

        FROM courses c
        LEFT JOIN homework h
            ON c.course_id = h.course_id
        LEFT JOIN courses_users cu
            ON cu.course_id = c.course_id
        LEFT JOIN submissions s
            ON s.homework_id = h.homework_id
           AND s.student_id = cu.user_id

        WHERE c.teacher_id = ?
        GROUP BY c.course_id, c.course_name
        ORDER BY c.course_name
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, teacherId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String,Object> row = new HashMap<>();
            row.put("courseId", rs.getInt("course_id"));
            row.put("courseName", rs.getString("course_name"));
            row.put("totalHomework", rs.getInt("total_homework"));
            row.put("overdue", rs.getInt("overdue_count"));
            list.add(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
    }   

    // =========================
    // GET COURSES BY STUDENT
    // =========================
    public List<Map<String, Object>> getCoursesByStudent(int studentId) {

    List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT c.course_id,
                c.course_name,
                u.name AS teacher_name
            FROM courses c
            JOIN courses_users cu ON c.course_id = cu.course_id
            LEFT JOIN users u ON c.teacher_id = u.user_id
            WHERE cu.user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("courseId", rs.getInt("course_id"));
                row.put("courseName", rs.getString("course_name"));
                row.put("teacherName", rs.getString("teacher_name"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

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
    // GET COURSE BY ID
    // =========================
    public Map<String, Object> getCourseById(int courseId) {
        String sql = "SELECT course_id, course_name FROM courses WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("courseId", rs.getInt("course_id"));
                row.put("courseName", rs.getString("course_name"));
                return row;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
