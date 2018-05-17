package com.kush.lib.location.services;

import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.google.PlaceSearchFailedException;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;

@Service
public class PlaceService extends BaseService {

    @ServiceMethod
    public Place findPlace(String text) throws PlaceSearchFailedException {
        PlaceFinder placeFinder = getInstance(PlaceFinder.class);
        return placeFinder.find(text);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(PlaceFinder.class);
    }
}
