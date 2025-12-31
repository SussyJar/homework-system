package com.example.servlet;

import java.io.IOException;

import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * AdminDashboardServlet - Controller untuk admin dashboard
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User admin = (User) req.getSession().getAttribute("user");

        // Set user sebagai attribute untuk JSP (optional)
        req.setAttribute("user", admin);

        // Forward ke JSP view
        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
