package com.sen3006.coursewise.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.server.Database;
import com.sen3006.coursewise.server.Main;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractHandler implements HttpHandler {
    protected final Gson gson = new Gson();
    protected final Database db = Database.getInstance();
    public void handle(com.sun.net.httpserver.HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String response;
        try {
            switch (method) {
                case "GET":
                    response = handleGet(exchange);
                    break;
                case "POST":
                    response = handlePost(exchange);
                    break;
                case "PUT":
                    response = handlePut(exchange);
                    break;
                case "DELETE":
                    response = handleDelete(exchange);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
                    break;
            }

            try(OutputStream os = exchange.getResponseBody()) {
                if (response == null){
                    response = "No content";
                    exchange.sendResponseHeaders(500, response.length());
                }
                os.write(response.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    // host/api/classroom/{id}
    protected String handleGet(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String response;

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        response = fetch(pathParts, query);
        exchange.sendResponseHeaders(200, response.getBytes().length);

        return response;
    }

    protected String handlePost(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        JsonObject requestBody;
        String response;
        Stream<String> stream = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines();

        // Read the request body
        StringBuilder sb = new StringBuilder();
        stream.forEach(sb::append);

        if(sb.isEmpty()) {
            response = "{\"message\": \"Invalid request\", \"status\": \"error\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            return response;
        }

        requestBody = gson.fromJson(sb.toString(), JsonObject.class).getAsJsonObject();

        response = add(requestBody);

        if (gson.fromJson(response, JsonObject.class).get("status").getAsString().contentEquals("success"))
            exchange.sendResponseHeaders(200, response.getBytes().length);
        else
            exchange.sendResponseHeaders(400, response.getBytes().length);
        return response;
    }

    protected String handlePut(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        JsonObject requestBody;
        String response;
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        Stream<String> stream = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines();

        // Read the request body
        StringBuilder sb = new StringBuilder();
        stream.forEach(sb::append);

        requestBody = gson.fromJson(sb.toString(), JsonObject.class).get("data").getAsJsonObject();

        if(pathParts.length < 4 || requestBody == null) {
            response = "{\"message\": \"Invalid request\", \"status\": \"error\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            return response;
        }

        response = update(pathParts, requestBody);

        if (gson.fromJson(response, JsonObject.class).get("status").getAsString().contentEquals("success"))
            exchange.sendResponseHeaders(200, response.getBytes().length);
        else
            exchange.sendResponseHeaders(404, response.getBytes().length);
        return response;
    }

    protected String handleDelete(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        return null;
    }


    protected Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0], keyValue[1]);
                }
            }
            return map;
        }return null;
    }

    protected abstract String add(JsonObject body);
    protected abstract String fetch(String[] path, String query);
    protected abstract String update(String[] path, JsonObject body);
}
