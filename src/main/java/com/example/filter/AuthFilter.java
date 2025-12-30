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
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/student/*", "/teacher/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get session
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }

        // Jika user belum login, redirect ke login
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Ambil path yang diakses
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Role-based access control
        String role = user.getRole();
        boolean authorized = false;

        if (path.startsWith("/student/") && "student".equals(role)) {
            authorized = true;
        } else if (path.startsWith("/teacher/") && "teacher".equals(role)) {
            authorized = true;
        } else if (path.startsWith("/admin/") && "admin".equals(role)) {
            authorized = true;
        }

        if (!authorized) {
            // User tidak memiliki akses, redirect ke halaman dashboard utama
            httpResponse.sendRedirect(contextPath + "/dashboard");
            return;
        }

        // User authorized, lanjutkan request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
