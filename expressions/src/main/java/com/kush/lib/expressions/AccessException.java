package com.kush.lib.expressions;

public class AccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public static AccessException exceptionWithMessage(String messageTemplate, Object... args) {
        return new AccessException(String.format(messageTemplate, args));
    }

    public AccessException() {
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(String message, Exception e) {
        super(message, e);
    }
}
