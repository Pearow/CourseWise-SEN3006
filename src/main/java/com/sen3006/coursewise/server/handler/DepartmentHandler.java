package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DepartmentHandler extends AbstractHandler {
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {
            response = db.fetchDepartment(Integer.parseInt(parseQuery(query).get("id")));
        } else if (pathParts.length == 4) {
            response = db.fetchDepartment(Integer.parseInt(pathParts[3]));
        } else {
            response = db.fetchDepartments();
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Department not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject body) {
        if (db.updateDepartment(Integer.parseInt(pathParts[3]), body)) {
            return "{\"message\": \"Department updated successfully\", \"status\": \"success\"}";
        }
        else {
            return "{\"message\": \"Department not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertDepartment(body)) {
            return "{\"message\": \"Department added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Department\", \"status\": \"error\"}";
        }
    }
}
