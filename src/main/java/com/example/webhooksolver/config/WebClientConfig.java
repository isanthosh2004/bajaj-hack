package com.example.webhooksolver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "app.webclient")
public class WebClientConfig {
    private Duration timeout = Duration.ofSeconds(30);
    private RetryConfig retry = new RetryConfig();
    
    @Data
    public static class RetryConfig {
        private int maxAttempts = 3;
        private Duration initial = Duration.ofSeconds(1);
        private double multiplier = 2.0;
        private Duration max = Duration.ofSeconds(10);
    }
}
