package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.server.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class ClassroomHandler extends AbstractHandler{

    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {//
            response = db.fetchClassroom(parseQuery(query).get("id"));
        } else if (pathParts.length == 4) {
            response = db.fetchClassroom(pathParts[3]);
        } else {
            response = db.fetchClassrooms();
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response.toString());
        else
            return "{\"message\": \"Classroom not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject requestBody) {
        if (db.updateClassroom(pathParts[3], requestBody))
            return  "{\"message\": \"User updated successfully\", \"status\": \"success\"}";//

        else
            return  "{\"message\": \"User not found\", \"status\": \"error\"}";//

    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertClassroom(body)) {
            return "{\"message\": \"Classroom added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert Classroom\", \"status\": \"error\"}";
        }
    }
}
