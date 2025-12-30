package com.example.servlet;

import com.example.dao.HomeworkReminderDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/api/student/reminder")
public class SetReminderServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // ===== SESSION CHECK =====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendJsonError(response, "Not logged in");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // ===== REQUEST DATA =====
            int homeworkId = Integer.parseInt(request.getParameter("homeworkId"));
            String remindAtStr = request.getParameter("remindAt");

            // Parse datetime (format: "YYYY-MM-DDTHH:mm")
            LocalDateTime remindAt = LocalDateTime.parse(remindAtStr);

            // ===== SAVE REMINDER =====
            HomeworkReminderDAO dao = new HomeworkReminderDAO();
            dao.saveReminder(homeworkId, user.getUserId(), remindAt);

            sendJsonSuccess(response, "Reminder saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonError(response, "Failed to save reminder: " + e.getMessage());
        }
    }
}


// @WebServlet("/api/student/reminder")
// public class SetReminderServlet extends HttpServlet {

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws IOException {

//         HttpSession session = request.getSession(false);
//         User user = (User) session.getAttribute("user");

//         int homeworkId = Integer.parseInt(request.getParameter("homeworkId"));
//         String remindAtStr = request.getParameter("remindAt");

//         HomeworkReminderDAO dao = new HomeworkReminderDAO();
//         dao.saveReminder(
//             homeworkId,
//             user.getUserId(),
//             LocalDateTime.parse(remindAtStr)
//         );

//         response.setContentType("application/json");
//         response.getWriter().print("""
//             {"status":"success","message":"Reminder saved"}
//         """);
//     }
// }
