package com.calicoapps.kumabudget;

import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.interceptor.AuthenticationInterceptor;
import com.calicoapps.kumabudget.interceptor.DeviceHeaderInterceptor;
import com.calicoapps.kumabudget.interceptor.PerformanceLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final PerformanceLoggingInterceptor performanceLoggingInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final DeviceHeaderInterceptor deviceHeaderInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET" , "PUT" , "POST" , "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(deviceHeaderInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/" , "/isAlive")
                .order(1);

        registry
                .addInterceptor(performanceLoggingInterceptor)
                .addPathPatterns(Constants.API_URL + "**")
                .excludePathPatterns(Constants.API_URL + "auth/**")
                .order(2);

        registry
                .addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/" ,
                        "/isAlive" ,
                        Constants.API_URL + "auth/**" ,
                        "/v2/api-docs" ,
                        "/v3/api-docs" ,
                        "/v3/api-docs/**" ,
                        "/swagger-resources" ,
                        "/swagger-resources/**" ,
                        "/configuration/ui" ,
                        "/configuration/security" ,
                        "/swagger-ui/**" ,
                        "/webjars/**" ,
                        "/swagger-ui.html"
                )
                .order(3);

    }

}