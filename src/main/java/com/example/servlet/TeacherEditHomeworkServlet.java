package com.example.servlet;

import com.example.dao.HomeworkDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet("/teacher/edit-homework")
public class TeacherEditHomeworkServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Session and role validation
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            req.setAttribute("error", "You are not authorized to access this page.");
            req.getRequestDispatcher("/teacher/dashboard.jsp").forward(req, resp);
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null) {
            req.setAttribute("error", "Homework ID is missing.");
            req.getRequestDispatcher("/teacher/homework").forward(req, resp);
            return;
        }

        try {
            int homeworkId = Integer.parseInt(idParam);
            HomeworkDAO dao = new HomeworkDAO();
            Map<String, Object> homework = dao.getHomeworkById(homeworkId);

            if (homework == null) {
                req.setAttribute("error", "Homework not found.");
                req.getRequestDispatcher("/teacher/homework").forward(req, resp);
                return;
            }

            // Convert timestamp to a format suitable for datetime-local input
            Timestamp deadlineTs = (Timestamp) homework.get("deadline");
            if (deadlineTs != null) {
                LocalDateTime deadlineLdt = deadlineTs.toLocalDateTime();
                String formattedDeadline = deadlineLdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                homework.put("formattedDeadline", formattedDeadline);
            }

            req.setAttribute("homework", homework);
            req.getRequestDispatcher("/teacher/edit_homework.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid Homework ID format.");
            req.getRequestDispatcher("/teacher/homework").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Session and role validation
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            // Should not happen if form is correctly pointing, but good for security
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String difficulty = req.getParameter("difficulty");
            String format = req.getParameter("format");

            String deadlineStr = req.getParameter("deadline"); // YYYY-MM-DDTHH:mm
            deadlineStr = deadlineStr.replace("T", " ") + ":00"; // to YYYY-MM-DD HH:mm:ss
            Timestamp deadline = Timestamp.valueOf(deadlineStr);

            HomeworkDAO dao = new HomeworkDAO();
            dao.updateHomework(id, title, description, difficulty, deadline, format);

            resp.sendRedirect(req.getContextPath() + "/teacher/homework");

        } catch (Exception e) {
            e.printStackTrace();
            // In a real app, redirect to an error page with a more user-friendly message
            resp.sendRedirect(req.getContextPath() + "/teacher/homework?error=updateFailed");
        }
    }
}
