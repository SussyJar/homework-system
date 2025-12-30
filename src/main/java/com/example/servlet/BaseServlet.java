package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * BaseServlet - Base class untuk semua servlet
 * Otomatis mengatur character encoding ke UTF-8 untuk request dan response
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Set UTF-8 encoding untuk request dan response
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Lanjutkan ke method doGet/doPost
        super.service(req, resp);
    }

    /**
     * Helper method untuk mengecek apakah user sudah login
     */
    protected boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession(false) != null &&
               req.getSession(false).getAttribute("user") != null;
    }

    /**
     * Helper method untuk mengecek role user
     */
    protected boolean hasRole(HttpServletRequest req, String role) {
        if (!isLoggedIn(req)) {
            return false;
        }
        com.example.model.User user = (com.example.model.User)
            req.getSession(false).getAttribute("user");
        return user != null && role.equalsIgnoreCase(user.getRole());
    }

    /**
     * Helper method untuk mengirim response JSON
     */
    protected void sendJsonResponse(HttpServletResponse resp, String json)
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(json);
    }

    /**
     * Helper method untuk mengirim error JSON
     */
    protected void sendJsonError(HttpServletResponse resp, String message)
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write("{\"success\": false, \"message\": \"" +
            escapeJson(message) + "\"}");
    }

    /**
     * Helper method untuk mengirim success JSON
     */
    protected void sendJsonSuccess(HttpServletResponse resp, String message)
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write("{\"success\": true, \"message\": \"" +
            escapeJson(message) + "\"}");
    }

    /**
     * Helper method untuk escape JSON string
     */
    protected String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
