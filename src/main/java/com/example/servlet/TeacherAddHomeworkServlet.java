package com.example.servlet;

import com.example.dao.CourseDAO;
import com.example.dao.HomeworkDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@WebServlet("/teacher/add-homework")
public class TeacherAddHomeworkServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        CourseDAO courseDAO = new CourseDAO();
        List<Map<String, Object>> courses = courseDAO.getCoursesByTeacher(teacher.getUserId());

        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/teacher/add_homework.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User teacher = (User) session.getAttribute("user");
        if (!"teacher".equals(teacher.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int courseId = Integer.parseInt(req.getParameter("courseId"));
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String difficulty = req.getParameter("difficulty");
            String format = req.getParameter("format");
            String deadlineStr = req.getParameter("deadline");

            // Convert YYYY-MM-DDTHH:mm to YYYY-MM-DD HH:mm:ss
            Timestamp deadline = Timestamp.valueOf(deadlineStr.replace("T", " ") + ":00");

            HomeworkDAO hwDAO = new HomeworkDAO();
            hwDAO.createHomework(
                teacher.getUserId(),
                courseId,
                title,
                description,
                difficulty,
                deadline,
                format
            );

            resp.sendRedirect(req.getContextPath() + "/teacher/homework");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/teacher/add-homework?error=true");
        }
    }
}
