package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.dao.LoginLogDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);

        if (user != null) {

            // =========================
            // 1. INSERT LOGIN LOG
            // =========================
            LoginLogDAO logDAO = new LoginLogDAO();
            logDAO.insertLoginLog(
                user.getUserId(),
                user.getRole()
            );

            // =========================
            // 2. CREATE SESSION
            // =========================
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            // =========================
            // 3. REDIRECT TO DASHBOARD
            // =========================
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } else {
            response.sendRedirect(
                request.getContextPath() + "/login.jsp?error=1"
            );
        }
    }
}


