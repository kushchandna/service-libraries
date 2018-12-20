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

public class HttpResponseGooglePlaceReader implements HttpResponseReader<Place> {

    private final HttpResponseStringReader stringReader;

    public HttpResponseGooglePlaceReader(HttpResponseStringReader stringReader) {
        this.stringReader = stringReader;
    }

    @Override
    public Place read(InputStream inputStream) {
        String stringResponse = stringReader.read(inputStream);

        JsonParser jsonParser = new JsonParser();

        JsonReader jsonReader = new JsonReader(new StringReader(stringResponse));
        jsonReader.setLenient(true);
        JsonElement jsonElement = jsonParser.parse(jsonReader);

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray resultsAsArray = jsonObject.get("results").getAsJsonArray();
        JsonObject firstResultJson = resultsAsArray.get(0).getAsJsonObject();
        String name = firstResultJson.getAsString();

        JsonObject locationJson = firstResultJson.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
        double latitude = locationJson.get("lat").getAsDouble();
        double longitude = locationJson.get("lng").getAsDouble();
        Location location = new Location(latitude, longitude);

        return new Place(name, location, name);
    }
}
