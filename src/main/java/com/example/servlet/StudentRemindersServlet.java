package com.example.servlet;

import java.io.IOException;
import java.util.List;

import com.example.model.User;
import com.example.dao.HomeworkReminderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * StudentRemindersServlet - Shows upcoming reminders for student
 */
@WebServlet("/student/reminders")
public class StudentRemindersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User user = (User) req.getSession().getAttribute("user");

        // Load upcoming reminders
        HomeworkReminderDAO dao = new HomeworkReminderDAO();
        List<String> reminders = dao.getUpcomingReminders(user.getUserId());

        // Set attributes for JSP
        req.setAttribute("reminders", reminders);

        // Forward to JSP
        req.getRequestDispatcher("/student/reminders.jsp").forward(req, resp);
    }
}
