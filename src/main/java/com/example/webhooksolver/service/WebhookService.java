package com.example.webhooksolver.service;

import com.example.webhooksolver.config.CandidateConfig;
import com.example.webhooksolver.config.EndpointConfig;
import com.example.webhooksolver.dto.WebhookRequest;
import com.example.webhooksolver.dto.WebhookResponse;
import com.example.webhooksolver.dto.SolutionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {
    
    private final WebClient webClient;
    private final CandidateConfig candidateConfig;
    private final EndpointConfig endpointConfig;
    
    public Mono<WebhookResponse> generateWebhook() {
        WebhookRequest request = WebhookRequest.builder()
                .name(candidateConfig.getName())
                .regNo(candidateConfig.getRegNo())
                .email(candidateConfig.getEmail())
                .build();
        
        log.info("Generating webhook for candidate: {} (Reg: {})", 
                maskString(candidateConfig.getName()), 
                maskString(candidateConfig.getRegNo()));
        
        return webClient.post()
                .uri(endpointConfig.getGenerateWebhook())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WebhookResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10)))
                .doOnSuccess(response -> {
                    log.info("Webhook generated successfully. Webhook URL: {}, Token: {}", 
                            response.getWebhook(), 
                            maskToken(response.getAccessToken()));
                })
                .doOnError(error -> {
                    log.error("Failed to generate webhook: {}", error.getMessage());
                });
    }
    
    public Mono<Boolean> submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        SolutionRequest request = SolutionRequest.builder()
                .finalQuery(finalQuery)
                .build();
        
        String submitUrl = webhookUrl != null ? webhookUrl : endpointConfig.getDefaultSubmit();
        log.info("Submitting solution to: {}", submitUrl);
        log.info("Using token: {}", maskToken(accessToken));
        
        return webClient.post()
                .uri(submitUrl)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .map(response -> {
                    log.info("Solution submitted successfully. Status: {}", response.getStatusCode());
                    return true;
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10)))
                .onErrorReturn(false)
                .doOnError(error -> {
                    log.error("Failed to submit solution: {}", error.getMessage());
                });
    }
    
    private String maskString(String input) {
        if (input == null || input.length() <= 8) {
            return input;
        }
        return input.substring(0, 4) + "..." + input.substring(input.length() - 4);
    }
    
    private String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return token;
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}
