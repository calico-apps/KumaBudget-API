package com.calicoapps.kumabudget.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    UNAUTHORIZED("401_INVALID_TOKEN" , HttpStatus.FORBIDDEN, "Token not valid, expired or empty"),
    NOT_FOUND("404_ITEM" , HttpStatus.NOT_FOUND, "Item not found"),
    INTERNAL_DB("500_DB" , HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while contacting the database"),
    INTERNAL_JSON("500_JSON" , HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while serializing Json (JsonUtil)");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(String errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}