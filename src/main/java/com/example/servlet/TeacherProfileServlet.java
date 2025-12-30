package com.example.servlet;

import com.example.model.User;
import com.example.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TeacherProfileServlet - Handles teacher profile management
 */
@WebServlet("/teacher/profile")
public class TeacherProfileServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User user = (User) req.getSession().getAttribute("user");

        // Set user attribute for JSP
        req.setAttribute("user", user);

        // Forward to JSP
        req.getRequestDispatcher("/teacher/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User user = (User) req.getSession().getAttribute("user");
        
        UserDAO dao = new UserDAO();
    

        String username = req.getParameter("username");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        String message = null;

        // Update username
        dao.updateUsername(user.getUserId(), username);

        // Update profile (name, email)
        dao.updateProfile(user.getUserId(), name, email);

        // Change password if provided
        if (oldPassword != null && !oldPassword.isBlank()
                && newPassword != null && !newPassword.isBlank()) {

            boolean ok = dao.changePassword(
                    user.getUserId(),
                    oldPassword,
                    newPassword
            );

            if (!ok) {
                message = "Old password is incorrect.";
            }
        }

        if (message == null) {
            message = "Profile updated successfully.";
        }

        // Set message attribute for JSP
        req.setAttribute("message", message);
        req.setAttribute("user", dao.getUserById(user.getUserId()));

        // Forward to JSP
        req.getRequestDispatcher("/teacher/profile.jsp").forward(req, resp);
    }
    
}
