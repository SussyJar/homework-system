package com.example.servlet;

import com.example.dao.CourseDAO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * StudentCoursesServlet - Controller untuk student view courses
 */
@WebServlet("/student/courses")
public class StudentCoursesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User student = (User) req.getSession().getAttribute("user");
        CourseDAO dao = new CourseDAO();

        List<Map<String, Object>> courses = dao.getCoursesByStudent(student.getUserId());

        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/student/courses.jsp").forward(req, resp);
    }
}
