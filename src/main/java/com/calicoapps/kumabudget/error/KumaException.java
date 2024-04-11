package com.calicoapps.kumabudget.error;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class KumaException extends RuntimeException {

    private ErrorCode errorCode;
    private String additionalMessage;

    public KumaException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public KumaException(ErrorCode errorCode, String additionalMessage) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.additionalMessage = additionalMessage;
    }

    public static <T> T orElseThrow(Optional<T> optional, String entityName, String ref) {
        return optional.orElseThrow(() ->
                new KumaException(ErrorCode.NOT_FOUND, entityName + " with ref [" + ref + "] not found"));
    }

    public static KumaException notFound(String entityName, long id) {
        return new KumaException(ErrorCode.NOT_FOUND, entityName + " with id [" + id + "] not found");
    }

}