package com.calicoapps.kumabudget.interceptor;

import com.calicoapps.kumabudget.monitor.LoggingHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class PerformanceLoggingInterceptor implements HandlerInterceptor {

    private final static String ATTRIBUTE_NAME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(ATTRIBUTE_NAME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        log.info("{} {} {} {}ms" , LoggingHelper.PERF_PREFIX, request.getMethod(), request.getRequestURI(), (System.currentTimeMillis() - (Long) request.getAttribute(ATTRIBUTE_NAME)));
    }

}