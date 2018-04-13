package com.kush.lib.location.google;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class GooglePlacesApiTest {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api";
    private static final String OUTPUT_JSON = "json";

    private static String apiKey;

    private GoogleApiConnection connection;

    @BeforeClass
    public static void beforeAllTests() throws Exception {
        apiKey = GoogleApiKeyLoader.loadKey();
    }

    @Before
    public void beforeEachTest() throws Exception {
        connection = new GoogleApiConnection(apiKey);
    }

    @After
    public void afterEachTest() throws Exception {
        connection.close();
    }

    @Test
    public void nearbySearch() throws Exception {
        String nearbySearchContext = "/place/nearbysearch";
        String apiUrl = BASE_URL + nearbySearchContext + "/" + OUTPUT_JSON;
        String result = connection.getResult(apiUrl, ImmutableMap.of(
                "location", "28.635318,77.367220",
                "radius", 500));
        System.out.println(result);
    }
}
