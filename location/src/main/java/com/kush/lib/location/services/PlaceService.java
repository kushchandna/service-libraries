package com.kush.lib.location.services;

import java.io.IOException;

import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.api.PlaceSearchFailedException;
import com.kush.lib.location.google.GoogleApiKeyLoader;
import com.kush.lib.location.google.GooglePoweredPlaceFinder;
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
        addIfDoesNotExist(PlaceFinder.class, createGooglePoweredPlaceFinder());
    }

    private GooglePoweredPlaceFinder createGooglePoweredPlaceFinder() {
        try {
            String apiKey = GoogleApiKeyLoader.loadKey();
            return new GooglePoweredPlaceFinder(apiKey);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
