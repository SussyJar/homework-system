package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/api/user/update-profile")
public class UpdateProfileServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User user = (User) session.getAttribute("user");

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            response.getWriter().print("""
                {"status":"error","message":"Invalid input"}
            """);
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateProfile(
                user.getUserId(),
                name,
                email
        );

        if (success) {
            // Update session user info
            user.setName(name);
            user.setEmail(email);

            response.getWriter().print("""
                {"status":"success","message":"Profile updated"}
            """);
        } else {
            response.getWriter().print("""
                {"status":"error","message":"Update failed"}
            """);
        }
    }
}
