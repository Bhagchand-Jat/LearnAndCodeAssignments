package com.geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OpenStreetMapGeocoding {

    private static final String BASE_GEOCODING_URL = "https://nominatim.openstreetmap.org/search";
    private static final String USER_AGENT_HEADER = "JavaGeocoderApp/1.0";

    public static void main(String[] args) {
        String placeName = readUserInput("Enter a place name: ");
        if (placeName == null || placeName.trim().isEmpty()) {
            System.out.println("Invalid input. Please enter a valid place name.");
            return;
        }

        try {
            String jsonGeocodingResponse = fetchGeocodingDataFromAPI(placeName);
            extractAndPrintCoordinates(jsonGeocodingResponse);
        } catch (Exception exception) {
            System.err.println("An error occurred while fetching geocoding data:");
            exception.printStackTrace();
        }
    }

    private static String readUserInput(String inputPrompt) {
        System.out.print(inputPrompt);
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            return consoleReader.readLine();
        } catch (Exception inputException) {
            System.err.println("Error reading input from user.");
            return null;
        }
    }

    private static String fetchGeocodingDataFromAPI(String placeName) throws Exception {
        String encodedPlaceName = URLEncoder.encode(placeName, "UTF-8");
        String fullGeocodingRequestUrl = BASE_GEOCODING_URL + "?q=" + encodedPlaceName + "&format=json&limit=1";

        URL requestUrl = new URL(fullGeocodingRequestUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) requestUrl.openConnection();
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("User-Agent", USER_AGENT_HEADER);

        try (InputStream responseStream = httpConnection.getInputStream();
             InputStreamReader streamReader = new InputStreamReader(responseStream);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {

            return bufferedReader.lines().collect(Collectors.joining());
        }
    }

    private static void extractAndPrintCoordinates(String jsonResponse) {
        JsonArray jsonResultsArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

        if (jsonResultsArray.size() > 0) {
            JsonObject firstLocationResult = jsonResultsArray.get(0).getAsJsonObject();
            String latitudeValue = firstLocationResult.get("lat").getAsString();
            String longitudeValue = firstLocationResult.get("lon").getAsString();

            System.out.println("Latitude: " + latitudeValue);
            System.out.println("Longitude: " + longitudeValue);
        } else {
            System.out.println("No results found for the given place.");
        }
    }
}
