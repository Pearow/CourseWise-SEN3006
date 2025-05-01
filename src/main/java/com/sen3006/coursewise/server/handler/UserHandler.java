package com.sen3006.coursewise.server.handler;

import com.sen3006.coursewise.server.Database;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class UserHandler extends AbstractHandler {
    @Override
    protected String handleGet(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        Database db = Database.getInstance();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
//        String response = """
//                {
//                "data": %s,
//                "status": "success"
//                }""".formatted(db.fetchUsers().toString());
        String response = db.fetchUsers().toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        System.out.println(response);

        return response;
    }

    @Override
    protected String handlePost(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        return null;
    }

    @Override
    protected String handlePut(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        return null;
    }

    @Override
    protected String handleDelete(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        return null;
    }
}
