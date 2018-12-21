package com.kush.lib.location.google;

import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.utils.http.HttpClient;
import com.kush.utils.http.HttpResponseStringReader;

public class GooglePoweredPlaceFinder implements PlaceFinder {

    private final String apiKey;

    private final HttpClient<Place> httpClient;

    public GooglePoweredPlaceFinder(String apiKey) {
        this.apiKey = apiKey;
        httpClient = createTextSearchHttpClient();
    }

    @Override
    public Place find(String text) throws PlaceSearchFailedException {
        try {
            return httpClient.getObject(ImmutableMap.of(
                    "query", text,
                    "key", apiKey));
        } catch (IOException e) {
            throw new PlaceSearchFailedException(e.getMessage(), e);
        }
    }

    private HttpClient<Place> createTextSearchHttpClient() {
        String textSearchApiUrl = GoogleApi.getTextSearchApiUrl();
        HttpResponseStringReader stringReader = new HttpResponseStringReader();
        HttpResponseGooglePlaceReader placeReader = new HttpResponseGooglePlaceReader(stringReader);
        return new HttpClient<>(textSearchApiUrl, placeReader);
    }
}
