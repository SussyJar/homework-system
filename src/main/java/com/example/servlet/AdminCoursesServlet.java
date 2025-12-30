package com.example.servlet;

import com.example.dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AdminCoursesServlet - Controller untuk manage courses
 */
@WebServlet("/admin/courses")
public class AdminCoursesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        CourseDAO dao = new CourseDAO();
        List<Map<String, Object>> courses = dao.getAllCourses();

        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/admin/courses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String courseName = req.getParameter("courseName");
        if (courseName != null && !courseName.trim().isEmpty()) {
            CourseDAO dao = new CourseDAO();
            dao.createCourse(courseName);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/courses");
    }
}
