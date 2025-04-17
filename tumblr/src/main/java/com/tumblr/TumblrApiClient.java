package com.tumblr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TumblrApiClient {
    public static String buildApiUrl(String blogName, int start, int end) {
        int totalPostsRequired = end - start + 1;
        return String.format("https://%s.tumblr.com/api/read/json?type=photo&num=%d&start=%d",
                              blogName, totalPostsRequired, start - 1);
    }

    public static String fetchApiResponse(String apiUrl) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder apiResponse = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                apiResponse.append(inputLine);
            }
            return apiResponse.toString();
        } finally {
            connection.disconnect();
        }
    }
}
