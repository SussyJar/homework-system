package com.example.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import com.example.model.User;
import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * StudentSubmissionsServlet - Shows submission details for a specific homework
 */
@WebServlet("/student/submissions")
public class StudentSubmissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User student = (User) req.getSession().getAttribute("user");

        // Required parameters
        String hwParam = req.getParameter("homeworkId");
        String courseParam = req.getParameter("courseId");

        if (hwParam == null || courseParam == null) {
            req.setAttribute("error", "Invalid access. Missing homeworkId or courseId.");
            req.getRequestDispatcher("/student/submissions.jsp").forward(req, resp);
            return;
        }

        int homeworkId = Integer.parseInt(hwParam);
        int courseId = Integer.parseInt(courseParam);

        // Load homework and submission
        HomeworkDAO hwDAO = new HomeworkDAO();
        SubmissionDAO subDAO = new SubmissionDAO();

        Map<String, Object> hw = hwDAO.getHomeworkById(homeworkId);
        Map<String, Object> sub = subDAO.getSubmissionByStudentAndHomework(
                student.getUserId(), homeworkId);

        if (hw == null) {
            req.setAttribute("error", "Homework not found.");
            req.getRequestDispatcher("/student/submissions.jsp").forward(req, resp);
            return;
        }

        // Check deadline status
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp deadline = (Timestamp) hw.get("deadline");
        boolean overdue = now.after(deadline);

        // Check if can resubmit (not overdue and status is 'submitted')
        boolean canResubmit = false;
        if (sub != null && !overdue && "submitted".equals(sub.get("status"))) {
            canResubmit = true;
        }

        // Set attributes for JSP
        req.setAttribute("homework", hw);
        req.setAttribute("submission", sub);
        req.setAttribute("courseId", courseId);
        req.setAttribute("overdue", overdue);
        req.setAttribute("canResubmit", canResubmit);

        // Forward to JSP
        req.getRequestDispatcher("/student/submissions.jsp").forward(req, resp);
    }
}
