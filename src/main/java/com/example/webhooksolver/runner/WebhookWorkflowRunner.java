package com.example.webhooksolver.runner;

import com.example.webhooksolver.service.WebhookOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookWorkflowRunner implements ApplicationRunner {
    
    private final WebhookOrchestrator orchestrator;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Application started. Executing webhook workflow...");
        
        // Execute the workflow asynchronously
        orchestrator.executeWorkflow()
                .subscribe(
                        result -> log.info("Webhook workflow execution completed"),
                        error -> log.error("Webhook workflow execution failed: {}", error.getMessage())
                );
    }
}
