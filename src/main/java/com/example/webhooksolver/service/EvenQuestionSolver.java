package com.example.webhooksolver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EvenQuestionSolver implements SqlQuestionSolver {
    
    @Override
    public String solveQuestion() {
        try {
            ClassPathResource resource = new ClassPathResource("answers/even.sql");
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.info("Loaded SQL solution for EVEN question from classpath");
            return sql.trim();
        } catch (IOException e) {
            log.warn("Failed to load even.sql from classpath, using fallback: {}", e.getMessage());
            return "SELECT 1;";
        }
    }
    
    @Override
    public String getQuestionType() {
        return "EVEN";
    }
}
