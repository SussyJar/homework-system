package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TeacherSubmissionsServlet - Controller untuk view student submissions
 */
@WebServlet("/teacher/submissions")
public class TeacherSubmissionsServlet extends HttpServlet {

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
            SubmissionDAO submissionDAO = new SubmissionDAO();
            HomeworkDAO homeworkDAO = new HomeworkDAO();

            List<Map<String, Object>> submissions = submissionDAO.getAllStudentsSubmissionStatus(homeworkId);
            Map<String, Object> homework = homeworkDAO.getHomeworkById(homeworkId);
            String homeworkTitle = (homework != null) ? (String) homework.get("title") : "Unknown";


            req.setAttribute("submissions", submissions);
            req.setAttribute("homeworkId", homeworkId);
            req.setAttribute("homeworkTitle", homeworkTitle);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid Homework ID.");
        }

        req.getRequestDispatcher("/teacher/submissions.jsp").forward(req, resp);
    }
}
