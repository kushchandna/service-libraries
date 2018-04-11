package com.kush.lib.location.google;

public class GoogleApi {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api";

    private static final String PLACE_API_NAME = "places/textsearch/json";

    public static String getPlacesApiUrl() {
        return getApiUrl(PLACE_API_NAME);
    }

    private static String getApiUrl(String apiName) {
        return BASE_URL + "/" + apiName;
    }
}
