package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;

@WebServlet("/api/admin/user")
public class AdminUserServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        // ===== SESSION & ROLE CHECK =====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User admin = (User) session.getAttribute("user");
        if (!"admin".equals(admin.getRole())) {
            response.getWriter().print("""
                {"status":"error","message":"Access denied"}
            """);
            return;
        }

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        // ===== CREATE USER =====
        if ("create".equals(action)) {

            boolean success = dao.createUser(
                request.getParameter("username"),
                request.getParameter("password"),
                request.getParameter("role"),
                request.getParameter("name"),
                request.getParameter("email")
            );

            response.getWriter().print(success
                ? "{\"status\":\"success\",\"message\":\"User created\"}"
                : "{\"status\":\"error\",\"message\":\"Create failed\"}"
            );
        }

        // ===== ENABLE USER =====
        else if ("enable".equals(action)) {

            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean success = dao.enableUser(userId);

            response.getWriter().print(success
                ? "{\"status\":\"success\",\"message\":\"User enabled\"}"
                : "{\"status\":\"error\",\"message\":\"Enable failed\"}"
            );
        }

        // ===== DISABLE USER =====
        else if ("disable".equals(action)) {

            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean success = dao.disableUser(userId);

            response.getWriter().print(success
                ? "{\"status\":\"success\",\"message\":\"User disabled\"}"
                : "{\"status\":\"error\",\"message\":\"Disable failed\"}"
            );
        }

        // ===== INVALID ACTION =====
        else {
            response.getWriter().print("""
                {"status":"error","message":"Invalid action"}
            """);
        }
        

            
    }
    
}
