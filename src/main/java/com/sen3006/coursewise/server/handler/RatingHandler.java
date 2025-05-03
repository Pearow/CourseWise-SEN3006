package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RatingHandler extends AbstractHandler {
    // host/api/{professor_id}/{user_id}
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("user_id") && query.contains("course_id")) {
            response = db.fetchRating(Integer.parseInt(parseQuery(query).get("user_id")), Integer.parseInt(parseQuery(query).get("course_id")));
        } else if (pathParts.length == 4) {
            response = db.fetchRatings(Integer.parseInt(pathParts[3]));
        } else if (pathParts.length == 5) {
            response = db.fetchRating(Integer.parseInt(pathParts[4]), Integer.parseInt(pathParts[3]));
        } else {
            return "{\"status\" : \"error\", message: \"Invalid request\"}";
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"status\" : \"error\", message: \"Rating not found\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject requestBody) {
        if (pathParts.length == 5 && db.updateRating(Integer.parseInt(pathParts[3]), Integer.parseInt(pathParts[4]), requestBody)) {
            return "{\"message\": \"Rating updated successfully\", \"status\": \"success\"}";
        } else if (pathParts.length < 5) {
            return "{\"message\": \"Invalid request\", \"status\": \"error\"}";
        } else {
            return "{\"message\": \"Rating not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertRating(body)) {
            return "{\"message\": \"Rating added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Rating\", \"status\": \"error\"}";
        }
    }
}
