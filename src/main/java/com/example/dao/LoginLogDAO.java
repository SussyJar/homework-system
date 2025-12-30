package com.example.dao;

import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginLogDAO {

    // =========================
    // INSERT LOGIN LOG
    // =========================
    public void insertLoginLog(int userId, String role) {

        String sql = """
            INSERT INTO login_logs (user_id, role)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, role);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ADMIN: get all login logs
    // =========================
    public List<Map<String, Object>> getAllLogs() {

        String sql = """
            SELECT l.log_id,
                   u.username,
                   l.role,
                   l.login_time
            FROM login_logs l
            LEFT JOIN users u ON l.user_id = u.user_id
            ORDER BY l.login_time DESC
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("logId", rs.getInt("log_id"));
                row.put("username", rs.getString("username"));
                row.put("role", rs.getString("role"));
                row.put("loginTime", rs.getTimestamp("login_time"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
