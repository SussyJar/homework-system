package com.example.servlet;

import com.example.model.User;
import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * StudentSubmitHomeworkServlet - Handles homework submission form
 */
@WebServlet("/student/submit")
public class StudentSubmitHomeworkServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User student = (User) req.getSession().getAttribute("user");

        // Required parameters
        String hwParam = req.getParameter("homeworkId");
        String courseParam = req.getParameter("courseId");

        if (hwParam == null || courseParam == null) {
            req.setAttribute("error", "Invalid request. Missing homeworkId or courseId.");
            req.getRequestDispatcher("/student/submit.jsp").forward(req, resp);
            return;
        }

        int homeworkId = Integer.parseInt(hwParam);
        int courseId = Integer.parseInt(courseParam);

        // Load homework details
        HomeworkDAO hwDAO = new HomeworkDAO();
        Map<String, Object> hw = hwDAO.getHomeworkById(homeworkId);

        if (hw == null) {
            req.setAttribute("error", "Homework not found.");
            req.getRequestDispatcher("/student/submit.jsp").forward(req, resp);
            return;
        }

        // Check if overdue
        Timestamp deadline = (Timestamp) hw.get("deadline");
        boolean overdue = deadline.before(new Timestamp(System.currentTimeMillis()));

        // Set attributes for JSP
        req.setAttribute("homework", hw);
        req.setAttribute("courseId", courseId);
        req.setAttribute("overdue", overdue);

        // Forward to JSP
        req.getRequestDispatcher("/student/submit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User student = (User) req.getSession().getAttribute("user");

        // Required parameters
        String hwParam = req.getParameter("homeworkId");
        String courseParam = req.getParameter("courseId");

        if (hwParam == null || courseParam == null) {
            resp.sendRedirect("dashboard");
            return;
        }

        int homeworkId = Integer.parseInt(hwParam);
        int courseId = Integer.parseInt(courseParam);

        // Check deadline
        HomeworkDAO hwDAO = new HomeworkDAO();
        Timestamp deadline = hwDAO.getDeadlineByHomeworkId(homeworkId);
        boolean overdue = deadline.before(new Timestamp(System.currentTimeMillis()));

        // Simple file path (as per assignment requirements)
        String filePath = "uploads/homework_" + homeworkId + "_" + student.getUserId();
        String status = overdue ? "late" : "submitted";

        // Insert/update submission
        SubmissionDAO subDAO = new SubmissionDAO();
        subDAO.insertSubmission(homeworkId, student.getUserId(), filePath, status);

        // Redirect back to homework list
        resp.sendRedirect("homework?courseId=" + courseId);
    }
}
