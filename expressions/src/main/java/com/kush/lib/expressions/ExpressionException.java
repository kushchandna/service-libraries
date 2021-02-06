package com.kush.lib.expressions;

public class ExpressionException extends Exception {

    private static final long serialVersionUID = 1L;

    public static ExpressionException exceptionWithMessage(String messageTemplate, Object... args) {
        return new ExpressionException(String.format(messageTemplate, args));
    }

    public ExpressionException() {
    }

    public ExpressionException(String message) {
        super(message);
    }

    public ExpressionException(String message, Exception e) {
        super(message, e);
    }
}
