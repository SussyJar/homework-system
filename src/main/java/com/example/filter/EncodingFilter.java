package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * EncodingFilter - Filter untuk mengatur UTF-8 encoding pada semua request dan response
 * Filter ini dijalankan sebelum semua servlet/JSP
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Bisa override encoding dari web.xml jika diperlukan
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Set encoding untuk request jika belum di-set
        if (httpRequest.getCharacterEncoding() == null) {
            httpRequest.setCharacterEncoding(encoding);
        }

        // Set encoding untuk response
        httpResponse.setCharacterEncoding(encoding);

        // Lanjutkan ke filter/servlet berikutnya
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup jika diperlukan
    }
}
