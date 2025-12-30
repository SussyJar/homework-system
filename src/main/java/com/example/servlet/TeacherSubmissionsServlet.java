package com.example.servlet;

import com.example.dao.SubmissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TeacherSubmissionsServlet - Controller untuk view student submissions
 */
@WebServlet("/teacher/submissions")
public class TeacherSubmissionsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String hwParam = req.getParameter("homeworkId");

        if (hwParam == null) {
            req.setAttribute("error", "Homework ID not found.");
            req.getRequestDispatcher("/teacher/submissions.jsp").forward(req, resp);
            return;
        }

        try {
            int homeworkId = Integer.parseInt(hwParam);
            SubmissionDAO dao = new SubmissionDAO();

            List<Map<String, Object>> submissions = dao.getAllStudentsSubmissionStatus(homeworkId);

            req.setAttribute("submissions", submissions);
            req.setAttribute("homeworkId", homeworkId);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid Homework ID.");
        }

        req.getRequestDispatcher("/teacher/submissions.jsp").forward(req, resp);
    }
}
