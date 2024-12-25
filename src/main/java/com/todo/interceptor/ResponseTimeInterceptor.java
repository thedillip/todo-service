package com.todo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ResponseTimeInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Incoming request: [{} {}]", request.getMethod(), request.getRequestURI());
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Request completed: [{} {}] executed in {} ms", request.getMethod(), request.getRequestURI(), duration);
    }
}