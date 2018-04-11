package com.kush.lib.location.google;

public class PlaceSearchFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PlaceSearchFailedException() {
    }

    public PlaceSearchFailedException(String message, Throwable e) {
        super(message, e);
    }
}
