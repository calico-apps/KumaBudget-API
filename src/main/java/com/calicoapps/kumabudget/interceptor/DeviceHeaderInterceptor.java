package com.calicoapps.kumabudget.interceptor;

import com.calicoapps.kumabudget.common.util.HeadersUtil;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class DeviceHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HeadersUtil.getValidDeviceFromRequestHeaders(request).isPresent()) {
            return true;
        } else {
            throw new KumaException(ErrorCode.BAD_REQUEST_DEVICE);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
    }

}