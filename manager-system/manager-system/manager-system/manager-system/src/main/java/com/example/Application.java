package com.example;

import com.jfinal.server.undertow.UndertowServer;
import com.example.config.AppConfig;

public class Application {
    public static void main(String[] args) {
        UndertowServer.start(AppConfig.class, 8080, true);
    }
} 