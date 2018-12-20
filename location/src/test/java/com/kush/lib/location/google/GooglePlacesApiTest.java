package com.kush.lib.location.google;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.lib.location.api.Place;
import com.kush.utils.http.HttpClient;
import com.kush.utils.http.HttpResponseReader;
import com.kush.utils.http.HttpResponseStringReader;

public class GooglePlacesApiTest {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api";
    private static final String OUTPUT_JSON = "json";

    private static String apiKey;

    private HttpClient<Place> client;

    @BeforeClass
    public static void beforeAllTests() throws Exception {
        apiKey = GoogleApiKeyLoader.loadKey();
    }

    @Before
    public void beforeEachTest() throws Exception {
        String apiUrl = prepareNearbySearchUrl();
        HttpResponseReader<Place> responseReader = createHttpResponsePlaceReader();
        client = new HttpClient<>(apiUrl, responseReader);
    }

    @Test
    public void nearbySearch() throws Exception {
        Place place = client.getObject(ImmutableMap.of(
                "location", "28.635318,77.367220",
                "radius", 500,
                "key", apiKey));
        System.out.println(place);
    }

    private String prepareNearbySearchUrl() {
        String nearbySearchContext = "/place/nearbysearch";
        return BASE_URL + nearbySearchContext + "/" + OUTPUT_JSON;
    }

    private HttpResponseReader<Place> createHttpResponsePlaceReader() {
        HttpResponseStringReader stringReader = new HttpResponseStringReader();
        return new HttpResponseGooglePlaceReader(stringReader);
    }
}
