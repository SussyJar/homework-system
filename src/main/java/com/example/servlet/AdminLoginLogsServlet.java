package com.example.servlet;

import com.example.dao.LoginLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/login_logs")
public class AdminLoginLogsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginLogDAO dao = new LoginLogDAO();
        List<Map<String, Object>> logs = dao.getAllLogs();
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/admin/login_logs.jsp").forward(req, resp);
    }
}
