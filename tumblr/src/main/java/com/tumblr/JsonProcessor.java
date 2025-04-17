package com.tumblr;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonProcessor {
	
    /**
     * Converts the Tumblr API response to a valid JSON string
     * by removing 'var tumblr_api_read = ' and trailing semicolon.
     */
    public static String convertToJsonResponse(String apiResponse) {
        return apiResponse.replaceFirst("var tumblr_api_read = ", "").replace(";", "");
    }

    public static void processJsonAndPrintData(String jsonResponse, int start) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONObject tumblelog = json.getJSONObject("tumblelog");
        JSONArray posts = json.getJSONArray("posts");

        printBlogInfo(tumblelog, json.getInt("posts-total"));
        printPostImages(posts, start);
    }

    private static void printBlogInfo(JSONObject tumblelog, int totalPosts) {
        System.out.println("\nTitle: " + tumblelog.getString("title"));
        System.out.println("Name: " + tumblelog.getString("name"));
        System.out.println("Description: " + tumblelog.getString("description"));
        System.out.println("Total Posts: " + totalPosts);
    }

    private static void printPostImages(JSONArray posts, int start) {
        int count = start;
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            printImageUrl(post, count);
            count++;
        }
    }

    private static void printImageUrl(JSONObject post, int count) {
        if (post.has("photo-url-1280")) {
            System.out.println(count + ". " + post.getString("photo-url-1280"));
        }

        if (post.has("photos")) {
            JSONArray photos = post.getJSONArray("photos");
            for (int j = 0; j < photos.length(); j++) {
                JSONObject photo = photos.getJSONObject(j);
                if (photo.has("photo-url-1280")) {
                    System.out.println("   " + photo.getString("photo-url-1280"));
                }
            }
        }
    }
}
