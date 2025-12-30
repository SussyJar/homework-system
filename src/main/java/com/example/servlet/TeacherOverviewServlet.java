package com.example.servlet;

import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/teacher/homework/overview")
public class TeacherOverviewServlet extends BaseServlet {

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

        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            response.getWriter().print("""
                {"status":"error","message":"Access denied"}
            """);
            return;
        }

        int homeworkId = Integer.parseInt(request.getParameter("homeworkId"));

        SubmissionDAO dao = new SubmissionDAO();

        var submitted = dao.getSubmittedStudents(homeworkId);
        var overdue = dao.getOverdueStudents(homeworkId);

        response.getWriter().print("""
            {
              "submitted": %s,
              "overdue": %s
            }
        """.formatted(submitted.toString(), overdue.toString()));
    }
}
