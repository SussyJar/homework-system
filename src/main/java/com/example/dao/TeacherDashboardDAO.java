package com.example.dao;

import com.example.util.DBConnection;
import java.sql.*;

public class TeacherDashboardDAO {

    // total course yang diajar teacher
    public int countCourses(int teacherId) {
        String sql = "SELECT COUNT(*) FROM courses WHERE teacher_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // homework yang masih aktif (deadline >= now)
    public int countActiveHomework(int teacherId) {
        String sql = """
            SELECT COUNT(*)
            FROM homework
            WHERE teacher_id = ?
              AND deadline >= NOW()
        """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // submission belum dinilai ATAU overdue
    public int countPendingSubmissions(int teacherId) {
        String sql = """
            SELECT COUNT(*)
            FROM submissions s
            JOIN homework h ON s.homework_id = h.homework_id
            WHERE h.teacher_id = ?
              AND (s.status != 'graded' OR s.status IS NULL)
        """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
