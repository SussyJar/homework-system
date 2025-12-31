package com.example.servlet;

import com.example.dao.CourseDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/assign_student")
public class AdminAssignStudentServlet extends HttpServlet {

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
            List<Map<String,Object>> students = dao.getAllStudents();

            req.setAttribute("course", course);
            req.setAttribute("students", students);
            req.getRequestDispatcher("/admin/assign_student.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Course ID.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            
            CourseDAO dao = new CourseDAO();
            dao.assignStudent(courseId, studentId);
            
            resp.sendRedirect(req.getContextPath() + "/admin/courses?message=Student+assigned+successfully");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID.");
        }
    }
}
