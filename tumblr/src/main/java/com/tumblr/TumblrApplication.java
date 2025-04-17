package com.tumblr;

import java.util.Scanner;

public class TumblrApplication {
	public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String blogName = InputHandler.getInput(scanner, "Enter the Tumblr blog name: ");

            int[] postRange;
            while (true) {
                String rangeInput = InputHandler.getInput(scanner, "Enter the range (start-end): ");
                postRange = InputHandler.parseRange(rangeInput);
                if (postRange != null) break;
            }

            String apiUrl = TumblrApiClient.buildApiUrl(blogName, postRange[0], postRange[1]);

            try {
                String apiResponse = TumblrApiClient.fetchApiResponse(apiUrl);
                String jsonResponse = JsonProcessor.convertToJsonResponse(apiResponse);
                JsonProcessor.processJsonAndPrintData(jsonResponse, postRange[0]);
            } catch (Exception e) {
                System.err.println("Error fetching data: " + e.getMessage());
            }
        }
    }

}
