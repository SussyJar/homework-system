package com.example.servlet;

import com.example.dao.SubmissionDAO;
import com.example.model.Submission;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/teacher/grade-submission")
public class GradeSubmissionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"teacher".equals(((User) session.getAttribute("user")).getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String submissionIdParam = req.getParameter("submissionId");
        if (submissionIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/teacher/homework?error=missingSubmissionId");
            return;
        }

        try {
            int submissionId = Integer.parseInt(submissionIdParam);
            SubmissionDAO dao = new SubmissionDAO();
            Map<String, Object> submission = dao.getSubmissionDetailsById(submissionId);

            if (submission == null) {
                resp.sendRedirect(req.getContextPath() + "/teacher/homework?error=submissionNotFound");
                return;
            }

            req.setAttribute("submission", submission);
            req.getRequestDispatcher("/teacher/grade_submission.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/teacher/homework?error=invalidSubmissionId");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"teacher".equals(((User) session.getAttribute("user")).getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int submissionId = Integer.parseInt(req.getParameter("submissionId"));
            int homeworkId = Integer.parseInt(req.getParameter("homeworkId"));
            int score = Integer.parseInt(req.getParameter("score"));
            String feedback = req.getParameter("feedback");

            SubmissionDAO dao = new SubmissionDAO();
            dao.gradeSubmission(submissionId, score, feedback);

            resp.sendRedirect(req.getContextPath() + "/teacher/submissions?homeworkId=" + homeworkId);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/teacher/homework?error=invalidParams");
        }
    }
}
