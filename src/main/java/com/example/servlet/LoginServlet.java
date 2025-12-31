package com.example.servlet;

import java.io.IOException;

import com.example.dao.UserDAO;
import com.example.dao.LoginLogDAO;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }
    
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
            String contextPath = request.getContextPath();
            switch (user.getRole()) {
                case "admin":
                    response.sendRedirect(contextPath + "/admin");
                    break;
                case "teacher":
                    response.sendRedirect(contextPath + "/teacher");
                    break;
                default:
                    response.sendRedirect(contextPath + "/student");
            }
        


        } else {
            response.sendRedirect(
                request.getContextPath() + "/login.jsp?error=1"
            );
        }
    }
}


