package com.example.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student/submissions")
public class StudentSubmissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        int studentId = user.getUserId();

        int homeworkId = Integer.parseInt(req.getParameter("homeworkId"));
        int courseId = Integer.parseInt(req.getParameter("courseId"));

        HomeworkDAO homeworkDAO = new HomeworkDAO();
        SubmissionDAO submissionDAO = new SubmissionDAO();

        Map<String, Object> homework = homeworkDAO.getHomeworkById(homeworkId);
        Map<String, Object> submission =
                submissionDAO.getSubmissionByStudentAndHomework(studentId, homeworkId);

        boolean overdue = false;
        boolean canResubmit = false;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp deadline = (Timestamp) homework.get("deadline");

        if (deadline.before(now)) {
            overdue = true;
        }

        if (submission != null && !"graded".equals(submission.get("status"))) {
            canResubmit = true;
        }

        // 🔥 SET ATTRIBUTE UNTUK JSP
        req.setAttribute("homework", homework);
        req.setAttribute("submission", submission);
        req.setAttribute("courseId", courseId);
        req.setAttribute("overdue", overdue);
        req.setAttribute("canResubmit", canResubmit);

        // 🔥 HALAMAN "MY SUBMISSION"
        req.getRequestDispatcher("/student/submissions.jsp")
           .forward(req, resp);
    }
}
