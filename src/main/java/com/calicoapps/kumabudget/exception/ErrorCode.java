package com.calicoapps.kumabudget.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST("400_ERROR_IN_REQUEST" , HttpStatus.BAD_REQUEST, "Something in your request is not valid"),
    BAD_REQUEST_DEVICE("400_BAD_DEVICE_HEADER" , HttpStatus.BAD_REQUEST, "Header Device is missing or invalid"),
    BAD_REQUEST_USER_ALREADY_EXIST("400_ERROR_USER_ALREADY_EXIST" , HttpStatus.BAD_REQUEST, "User with this email address already exists"),
    UNAUTHORIZED_TOKEN("401_INVALID_TOKEN" , HttpStatus.UNAUTHORIZED, "Token not valid, expired or empty"),
    UNAUTHORIZED_CREDENTIALS("401_BAD_CREDENTIALS" , HttpStatus.UNAUTHORIZED, "Credentials are incorrect"),
    NOT_FOUND("404_ITEM_NOT_FOUND" , HttpStatus.NOT_FOUND, "Item not found"),
    INTERNAL("500_ERROR_INTERNAL" , HttpStatus.INTERNAL_SERVER_ERROR, "An internal technical error occurred"),
    INTERNAL_DB("500_ERROR_DATABASE" , HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while contacting the database"),
    INTERNAL_JSON("500_ERROR_JSON" , HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while serializing Json (JsonUtil)");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(String errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}