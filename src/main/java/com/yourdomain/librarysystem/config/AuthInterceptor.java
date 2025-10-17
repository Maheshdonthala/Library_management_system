package com.yourdomain.librarysystem.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI();
        // Allow login and static resources
        if (path.startsWith("/login") || path.startsWith("/api") || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images") ) {
            return true;
        }
        if (session != null && session.getAttribute("user") != null) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
