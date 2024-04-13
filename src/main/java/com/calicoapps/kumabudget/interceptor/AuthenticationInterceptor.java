package com.calicoapps.kumabudget.interceptor;

import com.calicoapps.kumabudget.auth.service.TokenService;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.common.util.HeadersUtil;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("[AUTH] Authentication check for url {}" , request.getRequestURI());
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get(); // Can't be empty thanks to interceptor order
        Optional<String> tokenOpt = HeadersUtil.getValidTokenFromRequestHeaders(request);

        if (tokenOpt.isEmpty() || !tokenService.isTokenValid(tokenOpt.get(), false, device)) {
            throw new KumaException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
    }

}