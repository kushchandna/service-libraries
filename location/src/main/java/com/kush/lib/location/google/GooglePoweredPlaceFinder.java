package com.kush.lib.location.google;

import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.utils.http.HttpClient;
import com.kush.utils.http.HttpResponseStringReader;

public class GooglePoweredPlaceFinder implements PlaceFinder {

    public GooglePoweredPlaceFinder() {
    }

    @Override
    public Place find(String text) throws PlaceSearchFailedException {
        try {
            String key = GoogleApiKeyLoader.loadKey();
            String placesApiUrl = GoogleApi.getPlacesApiUrl();
            HttpResponseStringReader stringReader = new HttpResponseStringReader();
            HttpResponseGooglePlaceReader placeReader = new HttpResponseGooglePlaceReader(stringReader);
            HttpClient<Place> httpClient = new HttpClient<>(placesApiUrl, placeReader);
            return httpClient.getObject(ImmutableMap.of(
                    "query", text,
                    "key", key));
        } catch (IOException e) {
            throw new PlaceSearchFailedException(e.getMessage(), e);
        }
    }
}
