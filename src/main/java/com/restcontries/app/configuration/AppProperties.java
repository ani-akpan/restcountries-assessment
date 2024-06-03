package com.restcontries.app.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ConfigurationProperties(prefix = "app")
@Getter
@AllArgsConstructor
public class AppProperties {
    private Map<String, String> client;
    private String clientBaseUrl;
    private String clientContinent;

    public String getClientBaseUrl() {
        return client.get("baseUrl");
    }

    public String getClientContinent() {
        return client.get("continent");
    }
}
