package com.example.servlet;

import com.example.dao.HomeworkDAO;
import com.example.model.Homework;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/student/homework")
public class StudentHomeworkServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //  CEK SESSION
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            out.print("{\"status\":\"error\",\"message\":\"Not logged in\"}");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"student".equals(user.getRole())) {
            out.print("{\"status\":\"error\",\"message\":\"Access denied\"}");
            return;
        }

        //  AMBIL DATA HOMEWORK
        HomeworkDAO dao = new HomeworkDAO();
        List<Homework> list = dao.getAllHomework();

        //  RESPONSE JSON
        out.print("[");
            for (int i = 0; i < list.size(); i++) {
                Homework hw = list.get(i);
                out.print("{"
                    + "\"homeworkId\":" + hw.getHomeworkId() + ","
                    + "\"courseId\":" + hw.getCourseId() + ","
                    + "\"teacherId\":" + hw.getTeacherId() + ","
                    + "\"title\":\"" + hw.getTitle() + "\","
                    + "\"description\":\"" + hw.getDescription() + "\","
                    + "\"difficulty\":\"" + hw.getDifficulty() + "\","
                    + "\"deadline\":\"" + hw.getDeadline() + "\","
                    + "\"allowedFormat\":\"" + hw.getAllowedFormat() + "\""
                    + "}");
                if (i < list.size() - 1) out.print(",");
            }
            out.print("]");

    }
}
