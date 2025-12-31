package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.example.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * AdminUsersServlet - Controller untuk manage users
 * Handles: GET (list users), POST (enable/disable/reset actions)
 */
@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserDAO dao = new UserDAO();
        List<Map<String, Object>> users = dao.getAllUsers();

        // Set data untuk JSP
        req.setAttribute("users", users);

        // Forward ke JSP view
        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        UserDAO dao = new UserDAO();

        if ("create".equals(action)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String role = req.getParameter("role");
            String name = req.getParameter("name");
            String email = req.getParameter("email");

            // Basic validation
            if (username != null && !username.isBlank() && password != null && !password.isBlank() && role != null && !role.isBlank()) {
                boolean success = dao.createUser(
                    username.trim(),
                    password,
                    role,
                    name != null ? name.trim() : "",
                    email != null ? email.trim() : null
                );
                
                if (success) {
                    resp.sendRedirect(req.getContextPath() + "/admin/users?message=User+created+successfully");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/admin/create_user.jsp?error=Username+already+exists");
                }
                return;
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/create_user.jsp?error=Required+fields+are+missing");
                return;
            }
        }

        String userIdStr = req.getParameter("userId");
        if (action != null && userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                switch (action) {
                    case "disable":
                        dao.disableUser(userId);
                        break;
                    case "enable":
                        dao.enableUser(userId);
                        break;
                }
                 // Redirect back to the users list with a success message
                String message = "User+" + action + "d+successfully";
                resp.sendRedirect(req.getContextPath() + "/admin/users?message=" + message);
                return;
            } catch (NumberFormatException e) {
                // Invalid userId, ignore and redirect
            }
        }

        // Redirect back to the users list for any other case
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
