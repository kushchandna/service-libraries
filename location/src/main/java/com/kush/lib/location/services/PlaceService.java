package com.kush.lib.location.services;

import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.google.PlaceSearchFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;

@Service(name = "Place")
public class PlaceService extends BaseService {

    @ServiceMethod(name = "Find")
    public Place findPlace(String text) throws PlaceSearchFailedException {
        PlaceFinder placeFinder = getInstance(PlaceFinder.class);
        return placeFinder.find(text);
    }
}
