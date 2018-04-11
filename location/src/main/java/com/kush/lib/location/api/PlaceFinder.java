package com.kush.lib.location.api;

import com.kush.lib.location.google.PlaceSearchFailedException;

public interface PlaceFinder {

    Place find(String text) throws PlaceSearchFailedException;
}
