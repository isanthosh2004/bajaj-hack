package com.example.webhooksolver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.candidate")
    public CandidateConfig candidateConfig() {
        return new CandidateConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.endpoints")
    public EndpointConfig endpointConfig() {
        return new EndpointConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.webclient")
    public WebClientConfig webClientConfig() {
        return new WebClientConfig();
    }

    @Bean
    public WebClient webClient(WebClientConfig config) {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                        .build())
                .build();
    }
}
