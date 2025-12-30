package com.example.servlet;

import com.example.dao.CourseDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/assign_teacher")
public class AdminAssignTeacherServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseIdStr = req.getParameter("courseId");
        if (courseIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID not provided.");
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr);
            CourseDAO dao = new CourseDAO();
            
            Map<String, Object> course = dao.getCourseById(courseId);
            if (course == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found.");
                return;
            }
            List<Map<String,Object>> teachers = dao.getAllTeachers();

            req.setAttribute("course", course);
            req.setAttribute("teachers", teachers);
            req.getRequestDispatcher("/admin/assign_teacher.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Course ID.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            int teacherId = Integer.parseInt(req.getParameter("teacherId"));
            
            CourseDAO dao = new CourseDAO();
            dao.assignTeacher(courseId, teacherId);
            
            resp.sendRedirect(req.getContextPath() + "/admin/courses?message=Teacher+assigned+successfully");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID.");
        }
    }
}
