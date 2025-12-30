package com.example.dao;

import com.example.model.User;
import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    // =========================
    // LOGIN
    // =========================
    public User login(String username, String password) {

        String sql = """
            SELECT user_id, username, role, status
            FROM users
            WHERE username = ? AND password = ? AND status = 'active'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // CHANGE PASSWORD (SELF)
    // =========================
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        String sql = """
            UPDATE users
            SET password = ?
            WHERE user_id = ? AND password = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.setString(3, oldPassword);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // ADMIN: RESET PASSWORD
    // =========================
    public boolean resetPassword(int userId, String newPassword) {

        String sql = """
            UPDATE users
            SET password = ?
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // UPDATE PROFILE (ADMIN / SELF)
    // =========================
    public boolean updateProfile(int userId, String name, String email) {

        String sql = """
            UPDATE users
            SET name = ?, email = ?
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // CREATE USER (ADMIN)
    // =========================
    public boolean createUser(String username, String password,
                              String role, String name, String email) {

        String sql = """
            INSERT INTO users (username, password, role, name, email, status)
            VALUES (?, ?, ?, ?, ?, 'active')
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, name);
            ps.setString(5, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // DISABLE USER (ADMIN)
    // =========================
    public boolean disableUser(int userId) {

        String sql = """
            UPDATE users
            SET status = 'disabled'
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // ENABLE USER (ADMIN)
    // =========================
    public boolean enableUser(int userId) {

        String sql = """
            UPDATE users
            SET status = 'active'
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // ADMIN: GET ALL USERS
    // =========================
    public List<Map<String, Object>> getAllUsers() {

        String sql = """
            SELECT user_id, username, name, role, status
            FROM users
            ORDER BY user_id ASC
        """;

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("userId", rs.getInt("user_id"));
                row.put("username", rs.getString("username"));
                row.put("name", rs.getString("name"));
                row.put("role", rs.getString("role"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // ADMIN: GET USER BY ID
    // =========================
    public Map<String, Object> getUserById(int userId) {

        String sql = """
            SELECT user_id, username, name, email, role, status
            FROM users
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId", rs.getInt("user_id"));
                user.put("username", rs.getString("username"));
                user.put("name", rs.getString("name"));
                user.put("email", rs.getString("email"));
                user.put("role", rs.getString("role"));
                user.put("status", rs.getString("status"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // ADMIN: update username
    // =========================
    public boolean updateUsername(int userId, String username) {

        String sql = """
            UPDATE users
            SET username = ?
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
