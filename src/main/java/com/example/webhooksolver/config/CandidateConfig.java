package com.example.webhooksolver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.candidate")
public class CandidateConfig {
    private String name;
    private String regNo;
    private String email;
}
