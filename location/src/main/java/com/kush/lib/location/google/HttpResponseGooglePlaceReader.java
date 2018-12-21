package com.kush.lib.location.google;

import java.io.InputStream;
import java.io.StringReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.kush.lib.location.api.Location;
import com.kush.lib.location.api.Place;
import com.kush.utils.http.HttpResponseReader;
import com.kush.utils.http.HttpResponseStringReader;

class HttpResponseGooglePlaceReader implements HttpResponseReader<Place> {

    private final HttpResponseStringReader stringReader;

    public HttpResponseGooglePlaceReader(HttpResponseStringReader stringReader) {
        this.stringReader = stringReader;
    }

    @Override
    public Place read(InputStream inputStream) {
        String stringResponse = stringReader.read(inputStream);
        try {
            return parseAsPlace(stringResponse);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse " + stringResponse, e);
        }
    }

    private Place parseAsPlace(String stringResponse) {
        JsonElement jsonElement = parseJson(stringResponse);

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray resultsAsArray = jsonObject.get("results").getAsJsonArray();
        JsonObject firstResultJson = resultsAsArray.get(0).getAsJsonObject();

        String name = firstResultJson.get("name").getAsString();
        JsonObject locationJson = firstResultJson.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
        double latitude = locationJson.get("lat").getAsDouble();
        double longitude = locationJson.get("lng").getAsDouble();
        Location location = new Location(latitude, longitude);

        return new Place(name, location, name);
    }

    private JsonElement parseJson(String stringResponse) {
        JsonParser jsonParser = new JsonParser();
        JsonReader jsonReader = new JsonReader(new StringReader(stringResponse));
        jsonReader.setLenient(true);
        return jsonParser.parse(jsonReader);
    }
}
