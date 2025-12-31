package com.example.servlet;

import java.io.IOException;
import java.util.Map;

import com.example.dao.SubmissionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/teacher/grade-submission")
public class GradeSubmissionServlet extends HttpServlet {

    // =========================
    // GET → tampilkan form grading
    // =========================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String submissionIdParam = req.getParameter("submissionId");
        String homeworkIdParam = req.getParameter("homeworkId");

        if (submissionIdParam == null || homeworkIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/teacher/homework");
            return;
        }

        int submissionId = Integer.parseInt(submissionIdParam);

        SubmissionDAO dao = new SubmissionDAO();
        Map<String, Object> submission =
                dao.getSubmissionDetailsById(submissionId);

        if (submission == null) {
            req.setAttribute("error", "Submission not found");
        } else {
            req.setAttribute("submission", submission);
        }

        req.getRequestDispatcher("/teacher/grade_submission.jsp")
           .forward(req, resp);
    }

    // =========================
    // POST → simpan nilai
    // =========================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int submissionId = Integer.parseInt(req.getParameter("submissionId"));
        int homeworkId = Integer.parseInt(req.getParameter("homeworkId"));
        int score = Integer.parseInt(req.getParameter("score"));
        String feedback = req.getParameter("feedback");

        SubmissionDAO dao = new SubmissionDAO();

        // 🔥 SIMPAN NILAI
        dao.updateGrade(submissionId, score, feedback);

        // 🔥 REDIRECT BALIK KE LIST SUBMISSIONS
        resp.sendRedirect(
            req.getContextPath()
            + "/teacher/submissions?homeworkId=" + homeworkId
        );
    }
}
