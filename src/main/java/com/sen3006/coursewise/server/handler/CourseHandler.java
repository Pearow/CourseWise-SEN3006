package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CourseHandler extends AbstractHandler {
    // host/api/courses/{semester}
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {
            response = db.fetchCourse(parseQuery(query).get("id"));
        } else if (pathParts[2].contentEquals("courses")) {
            if (pathParts.length != 4){
                return "{\"message\": \"Invalid path\", \"status\": \"error\"}";
            }
            response = db.fetchCourses(Integer.parseInt(pathParts[3]));
        }
        else if (pathParts.length == 4) {
            response = db.fetchCourse(pathParts[3]);
        } else {
            response = db.fetchCourses();
        }
        if (response == null) {
            return "{\"message\": \"Course not found\", \"status\": \"error\"}";
        }else
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
    }

    @Override
    protected String update(String[] pathParts, JsonObject body) {
        if (db.updateCourse(pathParts[3], body)) {
            return "{\"message\": \"Course updated successfully\", \"status\": \"success\"}";
        }
        else {
            return "{\"message\": \"Course not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertCourse(body)) {
            return "{\"message\": \"Course added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Course\", \"status\": \"error\"}";
        }
    }
}
