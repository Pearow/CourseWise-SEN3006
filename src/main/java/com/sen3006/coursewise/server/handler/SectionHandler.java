package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SectionHandler extends AbstractHandler {
    // host/api/section/{course_id}/{semester}
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {
            response = db.fetchSection(Integer.parseInt(parseQuery(query).get("id")));
        } else if (pathParts.length == 5) {
            response = db.fetchSections(pathParts[3], Integer.parseInt(pathParts[4]));
        } else {
            return "{\"message\": Fetching all sections and fetching sections without semester value do not supported,\"status\": \"error\"}";
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Section not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject body) {
        if (db.updateSection(Integer.parseInt(pathParts[3]), body)) {
            return "{\"message\": \"Section updated successfully\", \"status\": \"success\"}";
        }
        else {
            return "{\"message\": \"Section not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertSection(body)) {
            return "{\"message\": \"Section added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Professor\", \"status\": \"error\"}";
        }
    }
}
