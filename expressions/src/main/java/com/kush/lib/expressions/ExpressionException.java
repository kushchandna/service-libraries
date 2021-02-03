package com.kush.lib.expressions;

public class ExpressionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExpressionException() {
    }

    public ExpressionException(String message, Exception e) {
        super(message, e);
    }
}
