package com.bridgelaz.bridgelabzlms.exception;

public class CustomServiceException extends Exception {
    private static final long serialVersionUID = -7688272877756877388L;

    public enum ExceptionType {
        NO_SUCH_USER,
        INVALID_EMAIL_ID,
        INVALID_TOKEN,
        DATA_NOT_FOUND,
        INVALID_ID,
        USER_DISABLED,
        INVALID_CREDENTIALS
    }

    public ExceptionType type;

    public CustomServiceException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }


}
