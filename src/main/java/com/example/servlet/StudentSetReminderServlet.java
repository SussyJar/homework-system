package com.example.servlet;

import com.example.model.User;
import com.example.dao.HomeworkReminderDAO;
    
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/student/set_reminder")
public class StudentSetReminderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int studentId  = user.getUserId();
        int homeworkId = Integer.parseInt(req.getParameter("homeworkId"));
        int courseId   = Integer.parseInt(req.getParameter("courseId"));

        // 🔔 default reminder: 1 hari dari sekarang
        LocalDateTime remindAt = LocalDateTime.now().plusDays(1);

        HomeworkReminderDAO dao = new HomeworkReminderDAO();
        dao.saveReminder(homeworkId, studentId, remindAt);

        // 🔙 balik ke halaman homework
        resp.sendRedirect(
            req.getContextPath() + "/student/homework?courseId=" + courseId
        );
    }
}
