package com.avegarlabs.construct_hub.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private int expirationMs;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(int expirationMs) {
        this.expirationMs = expirationMs;
    }
}