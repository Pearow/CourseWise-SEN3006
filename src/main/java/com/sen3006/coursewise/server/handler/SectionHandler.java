package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SectionHandler extends AbstractHandler {
    // host/api/{course_id}/{id}
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("user_id") && query.contains("course_id")) {
            response = db.fetchSection(Integer.parseInt(parseQuery(query).get("user_id")), parseQuery(query).get("course_id"));
        } else if (pathParts.length == 4) {
            response = db.fetchSections(Integer.parseInt(pathParts[3]));
        } else if (pathParts.length == 5) {
            response = db.fetchSection(Integer.parseInt(pathParts[3]), pathParts[4]);
        } else {
            return "{status : \"error\", message: \"Invalid request\"}";
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Section not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject requestBody) {
        if (pathParts.length == 5 && db.updateSection(Integer.parseInt(pathParts[3]), pathParts[4], requestBody)) {
            return "{\"message\": \"Section updated successfully\", \"status\": \"success\"}";
        } else if (pathParts.length < 5) {
            return "{\"message\": \"Invalid request\", \"status\": \"error\"}";
        } else {
            return "{\"message\": \"Section not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertSection(body)) {
            return "{\"message\": \"Section added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Section\", \"status\": \"error\"}";
        }
    }
}
