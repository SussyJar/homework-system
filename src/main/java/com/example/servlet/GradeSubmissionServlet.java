package com.example.servlet;

import com.example.dao.SubmissionDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/gradeSubmission")
public class GradeSubmissionServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int submissionId =
            Integer.parseInt(req.getParameter("submissionId"));

        int homeworkId =
            Integer.parseInt(req.getParameter("homeworkId"));

        int score =
            Integer.parseInt(req.getParameter("score"));

        String feedback =
            req.getParameter("feedback");

        SubmissionDAO dao = new SubmissionDAO();

        dao.gradeSubmission(
            submissionId,
            score,
            feedback
        );

        resp.sendRedirect(
            req.getContextPath() +
            "/teacher/submissions?homeworkId=" + homeworkId
        );
    }
}
