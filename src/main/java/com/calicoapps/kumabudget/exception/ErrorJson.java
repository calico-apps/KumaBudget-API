package com.calicoapps.kumabudget.exception;

import com.calicoapps.kumabudget.common.util.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorJson {

    private String errorCode;
    private String errorMessage;
    private String additionalMessage;

    public ErrorJson(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public ErrorJson(ErrorCode errorCode, String additionalMessage) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
        this.additionalMessage = additionalMessage;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}