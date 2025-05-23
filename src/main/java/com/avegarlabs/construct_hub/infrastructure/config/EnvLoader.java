package com.avegarlabs.construct_hub.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void load() {
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}
