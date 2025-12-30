package com.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
// import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}


// @WebServlet("/api/logout")
// public class LogoutServlet extends HttpServlet {

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {

//         response.setContentType("application/json;charset=UTF-8");

//         HttpSession session = request.getSession(false);
//         if (session != null) {
//             session.invalidate();
//         }

//         response.getWriter().print("""
//             {"status":"success","message":"Logged out"}
//         """);
//     }
// }
