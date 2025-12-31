package com.example.dao;

import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentHomeworkDAO {

    // Core method
   public List<Map<String, Object>> getHomeworkByStudent(int studentId, Integer courseId) {

    String sql = """
        SELECT h.homework_id,
               h.course_id,
               h.title,
               h.deadline,
               COALESCE(s.status, 'not_submitted') AS submissionStatus
        FROM courses_users cu
        JOIN homework h ON cu.course_id = h.course_id
        LEFT JOIN submissions s
            ON s.homework_id = h.homework_id
           AND s.student_id = cu.user_id
        WHERE cu.user_id = ?
          AND (? IS NULL OR h.course_id = ?)
        ORDER BY h.deadline ASC
    """;

    List<Map<String, Object>> list = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);

        if (courseId != null) {
            ps.setInt(2, courseId);
            ps.setInt(3, courseId);
        } else {
            ps.setNull(2, Types.INTEGER);
            ps.setNull(3, Types.INTEGER);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("homeworkId", rs.getInt("homework_id"));
            row.put("courseId", rs.getInt("course_id"));
            row.put("title", rs.getString("title"));
            row.put("deadline", rs.getTimestamp("deadline"));
            row.put("submissionStatus", rs.getString("submissionStatus"));
            list.add(row);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public List<Map<String, Object>> getAllHomeworkByStudent(int studentId) {
    return getHomeworkByStudent(studentId, null);
}

public List<Map<String, Object>> getHomeworkByCourse(int studentId, int courseId) {
    return getHomeworkByStudent(studentId, courseId);
}

}
