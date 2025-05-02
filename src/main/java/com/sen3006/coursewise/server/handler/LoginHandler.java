package com.sen3006.coursewise.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sen3006.coursewise.server.Database;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Stream;

public class LoginHandler implements HttpHandler {
    protected final Gson gson = new Gson();
    protected final Database db = Database.getInstance();
    public void handle(com.sun.net.httpserver.HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String response;
        try {
            if (method.equals("POST")) {
                response = handlePost(exchange);
            } else {
                response = "Method not allowed";
                exchange.sendResponseHeaders(405, response.length());
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

    protected String handlePost(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        JsonObject requestBody;
        String response;
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        Stream<String> stream = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines();

        // Read the request body
        StringBuilder sb = new StringBuilder();
        stream.forEach(sb::append);

        requestBody = gson.fromJson(sb.toString(), JsonObject.class).get("email").getAsJsonObject();

        if(requestBody == null || requestBody.get("email") == null) {
            response = "{\"message\": \"Invalid request\", \"status\": \"error\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            return response;
        }
        JsonObject data = db.fetchLogin(requestBody.get("email").getAsString()).getAsJsonObject();
        if(data == null) {
            response = "{\"message\": \"User not found\", \"status\": \"error\"}";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            return response;
        }else
            response = "{\"data\": " + data.getAsString() + ", \"status\": \"success\"}";

        return response;
    }
}
