package com.example.webhooksolver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.endpoints")
public class EndpointConfig {
    private String generateWebhook;
    private String defaultSubmit;
}
