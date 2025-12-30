package com.example.servlet;

import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet("/api/student/submit")
@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,      // 10MB
    maxRequestSize = 15 * 1024 * 1024
)
public class SubmitHomeworkServlet extends BaseServlet {

    private static final String UPLOAD_DIR = "C:/uploads/homework/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        // =========================
        // SESSION CHECK
        // =========================
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User student = (User) session.getAttribute("user");
        if (!"student".equals(student.getRole())) {
            response.getWriter().print("""
                {"status":"error","message":"Access denied"}
            """);
            return;
        }

        // =========================
        // REQUEST DATA
        // =========================
        int homeworkId = Integer.parseInt(request.getParameter("homeworkId"));
        Part filePart = request.getPart("file");

        if (filePart == null || filePart.getSize() == 0) {
            response.getWriter().print("""
                {"status":"error","message":"No file uploaded"}
            """);
            return;
        }

        // =========================
        // DEADLINE CHECK
        // =========================
        HomeworkDAO homeworkDAO = new HomeworkDAO();
        Timestamp deadline = homeworkDAO.getDeadlineByHomeworkId(homeworkId);

        String status = "submitted";
        Timestamp now = Timestamp.from(Instant.now());
        if (deadline != null && now.after(deadline)) {
            status = "late";
        }

        // =========================
        // SAVE FILE
        // =========================
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String safeFileName =
                student.getUserId() + "_" + homeworkId + "_" +
                filePart.getSubmittedFileName().replaceAll("\\s+", "_");

        File file = new File(uploadDir, safeFileName);

        Files.copy(
            filePart.getInputStream(),
            file.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        );

        // =========================
        // SAVE TO DATABASE
        // =========================
        SubmissionDAO submissionDAO = new SubmissionDAO();
        submissionDAO.insertSubmission(
            homeworkId,
            student.getUserId(),
            file.getAbsolutePath(),
            status
        );

        // =========================
        // RESPONSE
        // =========================
        response.getWriter().print("""
            {"status":"success","message":"Homework submitted"}
        """);
    }
}
