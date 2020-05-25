package com.bridgelaz.bridgelabzlms.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalControllerException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomServiceException.class)
    public final ResponseEntity<ApiError> handleAnyException(Exception ex, WebRequest request) {
        ApiError errorObj = new ApiError(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorObj, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
