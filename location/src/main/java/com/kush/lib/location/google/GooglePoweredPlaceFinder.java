package com.kush.lib.location.google;

import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.utils.commons.adapters.StringAdapter;
import com.kush.utils.http.HttpClient;

public class GooglePoweredPlaceFinder implements PlaceFinder {

    public GooglePoweredPlaceFinder() {
    }

    @Override
    public Place find(String text) throws PlaceSearchFailedException {
        try {
            String key = GoogleApiKeyLoader.loadKey();
            String placesApiUrl = GoogleApi.getPlacesApiUrl();

            StringAdapter adapter = null;
            HttpClient httpClient = new HttpClient(placesApiUrl, adapter);
            Object result = httpClient.getObject(Object.class, ImmutableMap.of(
                    "query", text,
                    "key", key));
            result.toString();
            return null;
        } catch (IOException e) {
            throw new PlaceSearchFailedException(e.getMessage(), e);
        }
    }
}
