package com.sen3006.coursewise.server;

import com.sen3006.coursewise.server.handler.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    HttpServer server;
    private String host;
    private int port;
    private boolean running = false;
    private boolean ready = false;

    public Main(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress(host, port), 0);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Main() {
        this("localhost", 3006);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isReady() {
        return ready;
    }

    public void start() {
        if (!running && ready) {
            server.start();
            running = true;
        }else if (!ready) {
            System.out.println("Server is not ready yet.");
        }
    }

    public void stop() {
        if (running) {
            server.stop(0);
            running = false;
        }else
            System.out.println("Server is not running.");
    }

    public void createContext() {
        server.createContext("/api/user", new UserHandler());


        // Test
        server.createContext("/api", exchange -> {
            String response = "{\"message\": \"Welcome to CourseWise\"," +
                    "\"status\": \"success\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        server.setExecutor(null); // creates a default executor
        ready = true;
    }
    public static void main(String[] args) throws Exception{
        Main server = new Main();
        System.out.printf("Listening on port %d\n", server.getPort());
        server.createContext();
        System.out.println("Server is ready.");
        server.start();
        System.out.println("Server is running.");
    }
}
