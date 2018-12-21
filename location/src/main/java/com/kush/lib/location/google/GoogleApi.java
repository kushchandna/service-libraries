package com.kush.lib.location.google;

class GoogleApi {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api";

    private static final String CONTEXT_TEXT_SEARCH = "place/textsearch/json";

    public static String getTextSearchApiUrl() {
        return getApiUrl(CONTEXT_TEXT_SEARCH);
    }

    private static String getApiUrl(String apiName) {
        return BASE_URL + "/" + apiName;
    }
}
