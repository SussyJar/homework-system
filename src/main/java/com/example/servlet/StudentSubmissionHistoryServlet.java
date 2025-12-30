package com.example.servlet;

import com.example.model.User;
import com.example.dao.SubmissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * StudentSubmissionHistoryServlet - Shows all submissions by student across all courses
 */
@WebServlet("/student/submission_history")
public class StudentSubmissionHistoryServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User user = (User) req.getSession().getAttribute("user");
        int studentId = user.getUserId();

        // Fetch all submissions by this student
        SubmissionDAO submissionDAO = new SubmissionDAO();
        List<Map<String, Object>> submissions = submissionDAO.getSubmissionsByStudent(studentId);

        // Set attributes untuk JSP
        req.setAttribute("submissions", submissions);

        // Forward ke JSP view
        req.getRequestDispatcher("/student/submission_history.jsp").forward(req, resp);
    }
}
