package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ProfessorHandler extends AbstractHandler {
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {
            response = db.fetchProfessor(Integer.parseInt(parseQuery(query).get("id")));
        } else if (pathParts.length == 4) {
            response = db.fetchProfessor(Integer.parseInt(pathParts[3]));
        } else {
            response = db.fetchProfessors();
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Professor not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject body) {
        if (db.updateProfessor(Integer.parseInt(pathParts[3]), body)) {
            return "{\"message\": \"Professor updated successfully\", \"status\": \"success\"}";
        }
        else {
            return "{\"message\": \"Professor not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertProfessor(body)) {
            return "{\"message\": \"Professor added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Professor\", \"status\": \"error\"}";
        }
    }
}
