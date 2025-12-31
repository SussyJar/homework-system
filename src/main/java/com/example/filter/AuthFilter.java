package com.example.filter;

import com.example.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter - Filter untuk authentication dan role-based access control
 * Mengecek apakah user sudah login dan memiliki role yang sesuai untuk mengakses halaman
 */
@WebFilter(urlPatterns = {"/student/*", "/teacher/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // BELUM LOGIN
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getRequestURI()
                         .substring(req.getContextPath().length());

        String role = user.getRole();
        boolean authorized =
                (path.startsWith("/student") && "student".equals(role)) ||
                (path.startsWith("/teacher") && "teacher".equals(role)) ||
                (path.startsWith("/admin") && "admin".equals(role));

        if (!authorized) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
