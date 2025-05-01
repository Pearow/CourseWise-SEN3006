package com.sen3006.coursewise.server.handler;

import com.sen3006.coursewise.server.Main;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractHandler implements HttpHandler {
    @Override
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

    protected abstract String handleGet(com.sun.net.httpserver.HttpExchange exchange) throws IOException;
    protected abstract String handlePost(com.sun.net.httpserver.HttpExchange exchange) throws IOException;
    protected abstract String handlePut(com.sun.net.httpserver.HttpExchange exchange) throws IOException;
    protected abstract String handleDelete(com.sun.net.httpserver.HttpExchange exchange) throws IOException;

}
