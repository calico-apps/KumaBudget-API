package com.calicoapps.kumabudget.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    //    org.postgresql.util.PSQLException
//    AuthenticationException
//    io.jsonwebtoken.MalformedJwtException
    @ExceptionHandler(value = {KumaException.class})
    protected ResponseEntity<Object> handleError(KumaException ex, WebRequest request) {
        if (ex.getAdditionalMessage() != null && !ex.getAdditionalMessage().isEmpty()) {
            return new ResponseEntity<>(new ErrorJson(ex.getErrorCode(), ex.getAdditionalMessage()), ex.getErrorCode().getHttpStatus());
        }
        return new ResponseEntity<>(new ErrorJson(ex.getErrorCode()), ex.getErrorCode().getHttpStatus());
    }
}