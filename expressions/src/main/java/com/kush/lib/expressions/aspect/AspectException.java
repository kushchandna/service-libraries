package com.kush.lib.expressions.aspect;

public class AspectException extends Exception {

    private static final long serialVersionUID = 1L;

    public static AspectException exceptionWithMessage(String messageTemplate, Object... args) {
        return new AspectException(String.format(messageTemplate, args));
    }

    public AspectException() {
    }

    public AspectException(String message) {
        super(message);
    }

    public AspectException(String message, Exception e) {
        super(message, e);
    }
}
