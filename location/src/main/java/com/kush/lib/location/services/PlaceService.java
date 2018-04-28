package com.kush.lib.location.services;

import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.google.PlaceSearchFailedException;
import com.kush.lib.service.server.BaseService;

public class PlaceService extends BaseService {

    public Place findPlace(String text) throws PlaceSearchFailedException {
        PlaceFinder placeFinder = getInstance(PlaceFinder.class);
        return placeFinder.find(text);
    }
}
