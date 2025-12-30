package com.example.servlet;

import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/student/submissions")
public class StudentSubmissionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // ===== SESSION CHECK =====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User student = (User) session.getAttribute("user");
        if (!"student".equals(student.getRole())) {
            response.getWriter().print("""
                {"status":"error","message":"Access denied"}
            """);
            return;
        }

        SubmissionDAO dao = new SubmissionDAO();
        var submissions = dao.getSubmissionsByStudent(student.getUserId());

        response.getWriter().print(submissions.toString());
    }
}
