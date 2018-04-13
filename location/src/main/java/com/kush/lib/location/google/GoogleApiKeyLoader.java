package com.kush.lib.location.google;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GoogleApiKeyLoader {

    private static final String API_FILE_NAME = "google_places_api_key.dat";
    private static final Path API_FILE_PATH = Paths.get("src", "test", "resources", API_FILE_NAME);

    public static String loadKey() throws IOException {
        List<String> allLines = Files.readAllLines(API_FILE_PATH);
        return allLines.get(0);
    }
}
