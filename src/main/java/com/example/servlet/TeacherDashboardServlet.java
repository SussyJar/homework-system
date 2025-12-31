package com.example.servlet;

import java.io.IOException;

import com.example.dao.TeacherDashboardDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * TeacherDashboardServlet - Controller untuk teacher dashboard
 */
@WebServlet("/teacher")
public class TeacherDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User teacher = (User) req.getSession().getAttribute("user");

        // Fetch dashboard data
        TeacherDashboardDAO dashDAO = new TeacherDashboardDAO();
        int courseCount = dashDAO.countCourses(teacher.getUserId());
        int hwCount = dashDAO.countActiveHomework(teacher.getUserId());
        int pendingCount = dashDAO.countPendingSubmissions(teacher.getUserId());

        // Set attributes untuk JSP
        req.setAttribute("courseCount", courseCount);
        req.setAttribute("hwCount", hwCount);
        req.setAttribute("pendingCount", pendingCount);

        // Forward ke JSP view
        req.getRequestDispatcher("/teacher/dashboard.jsp").forward(req, resp);
    }
}
