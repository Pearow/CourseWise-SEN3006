package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ReviewHandler extends AbstractHandler {
    // host/api/review/{course_id}/{user_id}
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("user_id") && query.contains("course_id")) {
            response = db.fetchReview(Integer.parseInt(parseQuery(query).get("user_id")), parseQuery(query).get("course_id"));
        } else if (pathParts.length == 4) {
            response = db.fetchReviews(pathParts[3]);
        } else if (pathParts.length == 5) {
            response = db.fetchReview(Integer.parseInt(pathParts[4]), pathParts[3]);
        } else {
            return "{status : \"error\", message: \"Invalid request\"}";
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Review not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject requestBody) {
        if (pathParts.length == 5 && db.updateReview(Integer.parseInt(pathParts[3]), pathParts[4], requestBody)) {
            return "{\"message\": \"Review updated successfully\", \"status\": \"success\"}";
        } else if (pathParts.length < 5) {
            return "{\"message\": \"Invalid request\", \"status\": \"error\"}";
        } else {
            return "{\"message\": \"Review not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertReview(body)) {
            return "{\"message\": \"Review added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Review\", \"status\": \"error\"}";
        }
    }
}
