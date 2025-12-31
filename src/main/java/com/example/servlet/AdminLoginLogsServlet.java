package com.example.servlet;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.example.dao.LoginLogDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet("/admin/login_logs")
public class AdminLoginLogsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginLogDAO dao = new LoginLogDAO();
        List<Map<String, Object>> logs = dao.getAllLogs();
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/admin/login_logs.jsp").forward(req, resp);
    }
}
