package com.kush.lib.location.google;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kush.lib.location.api.Place;

public class GooglePoweredPlaceFinderTest {

    private static String apiKey;

    private GooglePoweredPlaceFinder placeFinder;

    @BeforeClass
    public static void beforeAllTests() throws Exception {
        apiKey = GoogleApiKeyLoader.loadKey();
    }

    @Before
    public void beforeEachTest() throws Exception {
        placeFinder = new GooglePoweredPlaceFinder(apiKey);
    }

    @Test
    public void nearbySearch() throws Exception {
        Place place = placeFinder.find("delhi");
        System.out.println(place);
    }
}
