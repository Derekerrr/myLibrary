package com.example.mylibrary.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("user") == null)
        {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
