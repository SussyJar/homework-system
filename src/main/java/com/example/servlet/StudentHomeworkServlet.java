package com.example.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.dao.HomeworkDAO;
import com.example.dao.StudentHomeworkDAO;
import com.example.dao.SubmissionDAO;
import com.example.model.User;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student/homework")
public class StudentHomeworkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        int studentId = user.getUserId();

        String courseIdParam = req.getParameter("courseId");

        StudentHomeworkDAO dao = new StudentHomeworkDAO();
        List<Map<String, Object>> homeworkList;

        if (courseIdParam == null) {
            homeworkList = dao.getAllHomeworkByStudent(studentId);
            req.setAttribute("viewMode", "ALL");
        } else {
            int courseId = Integer.parseInt(courseIdParam);
            homeworkList = dao.getHomeworkByCourse(studentId, courseId);
            req.setAttribute("viewMode", "COURSE");
        }

        // 🔥 INI YANG KAMU LUPA
        req.setAttribute("homeworkList", homeworkList);

        req.getRequestDispatcher("/student/homework.jsp")
           .forward(req, resp);
    }
}
