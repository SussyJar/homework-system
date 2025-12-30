package com.example.servlet;

import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/teacher/submissions")
public class TeacherViewSubmissionsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("{\"status\":\"error\",\"message\":\"Not logged in\"}");
            return;
        }

        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            response.getWriter().print("{\"status\":\"error\",\"message\":\"Access denied\"}");
            return;
        }

        int homeworkId = Integer.parseInt(request.getParameter("homeworkId"));

        SubmissionDAO dao = new SubmissionDAO();
        List<Map<String, Object>> list = dao.getAllStudentsSubmissionStatus(homeworkId);


        // simple JSON output
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> r = list.get(i);
            json.append(String.format("""
                {
                  "submissionId": %d,
                  "studentId": %d,
                  "username": "%s",
                  "name": "%s",
                  "submitTime": "%s",
                  "filePath": "%s",
                  "score": %s,
                  "feedback": %s
                }
            """,
            r.get("submissionId"),
            r.get("studentId"),
            r.get("username"),
            r.get("name"),
            r.get("submitTime"),
            r.get("filePath"),
            r.get("score") == null ? "null" : r.get("score"),
            r.get("feedback") == null ? "null" : "\"" + r.get("feedback") + "\""
            ));
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");

        response.getWriter().print(json.toString());
    }
}
