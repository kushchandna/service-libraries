package com.kush.lib.location.google;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;

public class GooglePoweredPlaceFinder implements PlaceFinder {

    @Override
    public Place find(String text) throws PlaceSearchFailedException {
        try {
            String key = GoogleApiKeyLoader.loadKey();
            String urlWithParameters = GoogleApi.getUrlWithParameters("places/textsearch/json", ImmutableMap.of(
                    "query", text,
                    "key", key));
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(urlWithParameters);
            HttpResponse response = client.execute(request);
            response.toString();
            return null;
        } catch (IOException e) {
            throw new PlaceSearchFailedException(e.getMessage(), e);
        }
    }
}
