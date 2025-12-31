package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * StudentHomeworkListServlet - Controller untuk student view homework list
 */
@WebServlet("/student/homework")
public class StudentHomeworkListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User student = (User) req.getSession().getAttribute("user");
        String courseIdStr = req.getParameter("courseId");

        if (courseIdStr == null) {
            req.setAttribute("error", "Course not specified.");
            req.getRequestDispatcher("/student/homework.jsp").forward(req, resp);
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr);

            HomeworkDAO hwDAO = new HomeworkDAO();
            SubmissionDAO subDAO = new SubmissionDAO();

            List<Map<String, Object>> homeworkList = hwDAO.getHomeworkByCourse(courseId);

            // Untuk setiap homework, cek submission status
            for (Map<String, Object> hw : homeworkList) {
                int homeworkId = (int) hw.get("homeworkId");
                Map<String, Object> submission = subDAO.getSubmissionByStudentAndHomework(
                        student.getUserId(), homeworkId);

                if (submission != null) {
                    hw.put("submissionStatus", submission.get("status"));
                    hw.put("hasSubmission", true);
                } else {
                    hw.put("submissionStatus", "Pending");
                    hw.put("hasSubmission", false);
                }
            }

            req.setAttribute("homeworkList", homeworkList);
            req.setAttribute("courseId", courseId);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid course ID.");
        }

        req.getRequestDispatcher("/student/homework.jsp").forward(req, resp);
    }
}
