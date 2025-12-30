package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/api/user/change-password")
public class ChangePasswordServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().print("""
                {"status":"error","message":"Not logged in"}
            """);
            return;
        }

        User user = (User) session.getAttribute("user");

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        if (oldPassword == null || newPassword == null || newPassword.length() < 4) {
            response.getWriter().print("""
                {"status":"error","message":"Invalid password"}
            """);
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.changePassword(
                user.getUserId(),
                oldPassword,
                newPassword
        );

        if (success) {
            response.getWriter().print("""
                {"status":"success","message":"Password changed"}
            """);
        } else {
            response.getWriter().print("""
                {"status":"error","message":"Old password incorrect"}
            """);
        }
    }
}
