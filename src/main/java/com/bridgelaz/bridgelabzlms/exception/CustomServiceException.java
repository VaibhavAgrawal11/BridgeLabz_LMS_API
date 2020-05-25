package com.bridgelaz.bridgelabzlms.exception;

public class CustumServiceException extends Exception {
    public enum ExceptionType {
        NO_SUCH_USER,
        INVALID_EMAIL_ID,
        INVALID_PASSWORD,
        INVALID_TOKEN,
        DATA_NOT_FOUND,
        INVALID_ID
    }

    public ExceptionType type;

    public CustumServiceException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }


}
