package com.example.servlet;

import com.example.dao.HomeworkReminderDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/student/reminders")
public class ViewReminderServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // ===== SESSION CHECK =====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User user = (User) session.getAttribute("user");

        HomeworkReminderDAO dao = new HomeworkReminderDAO();
        var reminders = dao.getUpcomingReminders(user.getUserId());

        response.getWriter().print(reminders.toString());
    }
}
