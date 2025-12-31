package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.dao.HomeworkDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * TeacherHomeworkServlet - Controller untuk manage homework
 * Handles: GET (list homework), POST (delete action)
 */
@WebServlet("/teacher/homework")
public class TeacherHomeworkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User teacher = (User) req.getSession().getAttribute("user");
        HomeworkDAO dao = new HomeworkDAO();

        List<Map<String, Object>> homeworkList = dao.getHomeworkByTeacher(teacher.getUserId());

        // Set data untuk JSP
        req.setAttribute("homeworkList", homeworkList);

        // Forward ke JSP view
        req.getRequestDispatcher("/teacher/homework.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if ("delete".equals(action) && idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                HomeworkDAO dao = new HomeworkDAO();
                dao.deleteHomework(id);
            } catch (NumberFormatException e) {
                // Invalid id, ignore
            }
        }

        // Redirect ke halaman homework
        resp.sendRedirect("homework");
    }
}
