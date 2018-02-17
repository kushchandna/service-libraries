package com.kush.lib.auth;

public class KeyGenerationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public KeyGenerationFailedException() {
    }

    public KeyGenerationFailedException(String message) {
        super(message);
    }

    public KeyGenerationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
