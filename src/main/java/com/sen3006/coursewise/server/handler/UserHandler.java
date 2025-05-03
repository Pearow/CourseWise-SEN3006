package com.sen3006.coursewise.server.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.server.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;


public class UserHandler extends AbstractHandler {
    @Override
    protected String fetch(String[] pathParts, String query) {
        JsonElement response;
        if (query != null && query.contains("id")) {
            response = db.fetchUser(Integer.parseInt(parseQuery(query).get("id")));
        } else if (pathParts.length == 4) {
            response = db.fetchUser(Integer.parseInt(pathParts[3]));
        } else {
            response = db.fetchUsers();
        }
        if (response != null)
            return "{\"data\": %s,\"status\": \"success\"}".formatted(response);
        else
            return "{\"message\": \"User not found\", \"status\": \"error\"}";
    }

    @Override
    protected String update(String[] pathParts, JsonObject body) {
        if (db.updateUser(Integer.parseInt(pathParts[3]), body)) {
            return "{\"message\": \"User updated successfully\", \"status\": \"success\"}";
        }
        else {
            return "{\"message\": \"User not found\", \"status\": \"error\"}";
        }
    }

    @Override
    protected String add(JsonObject body) {
        if (db.insertUser(body)) {
            return "{\"message\": \"User added successfully\", \"status\": \"success\"}";
        } else {
            return "{\"message\": \"Failed to insert User\", \"status\": \"error\"}";
        }
    }
}

