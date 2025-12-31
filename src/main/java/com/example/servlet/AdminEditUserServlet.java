package com.example.servlet;

import com.example.dao.UserDAO;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/edit_user")
public class AdminEditUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("userId");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID not provided.");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            UserDAO dao = new UserDAO();
            Map<String, Object> user = dao.getUserById(userId);

            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }

            req.setAttribute("user", user);
            req.getRequestDispatcher("/admin/edit_user.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID format.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("userId");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID not provided.");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            String username = req.getParameter("username");
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            UserDAO dao = new UserDAO();
            dao.updateUsername(userId, username);
            dao.updateProfile(userId, name, email);

            if (password != null && !password.trim().isEmpty()) {
                dao.resetPassword(userId, password);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/users?message=User+updated+successfully");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID format.");
        }
    }
}
