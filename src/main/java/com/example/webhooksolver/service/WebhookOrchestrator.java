package com.example.webhooksolver.service;

import com.example.webhooksolver.config.CandidateConfig;
import com.example.webhooksolver.entity.Submission;
import com.example.webhooksolver.entity.Submission.AssignmentType;
import com.example.webhooksolver.entity.Submission.SubmissionStatus;
import com.example.webhooksolver.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookOrchestrator {
    
    private final WebhookService webhookService;
    private final SubmissionRepository submissionRepository;
    private final CandidateConfig candidateConfig;
    private final List<SqlQuestionSolver> questionSolvers;
    
    public Mono<Void> executeWorkflow() {
        log.info("Starting webhook workflow execution...");
        
        return webhookService.generateWebhook()
                .flatMap(webhookResponse -> {
                    log.info("Webhook generated, proceeding to solve question...");
                    
                    // Force EVEN assignment type for Question 2
                    AssignmentType assignmentType = AssignmentType.EVEN;
                    log.info("Assignment type set to EVEN for registration number: {}", 
                            maskString(candidateConfig.getRegNo()));
                    
                    // Log Google Drive link for Question 2
                    logGoogleDriveLink();
                    
                    // Solve the question
                    String finalQuery = solveQuestion(assignmentType);
                    log.info("Question solved. Final SQL query: {}", finalQuery);
                    
                    // Create and save submission
                    Submission submission = createSubmission(assignmentType, finalQuery);
                    submissionRepository.save(submission);
                    log.info("Submission saved with ID: {}", submission.getId());
                    
                    // Submit solution
                    return webhookService.submitSolution(
                            webhookResponse.getWebhook(), 
                            webhookResponse.getAccessToken(), 
                            finalQuery
                    ).flatMap(success -> {
                        // Update submission status
                        submission.setStatus(success ? SubmissionStatus.SUBMITTED : SubmissionStatus.FAILED);
                        submissionRepository.save(submission);
                        log.info("Submission status updated to: {}", submission.getStatus());
                        return Mono.empty();
                    });
                })
                .doOnSuccess(result -> {
                    log.info("Webhook workflow completed successfully");
                })
                .doOnError(error -> {
                    log.error("Webhook workflow failed: {}", error.getMessage());
                })
                .then();
    }
    
    private void logGoogleDriveLink() {
        log.info("Question 2 Google Drive link: https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing");
    }
    
    private String solveQuestion(AssignmentType assignmentType) {
        return questionSolvers.stream()
                .filter(solver -> "EVEN".equals(solver.getQuestionType()))
                .findFirst()
                .map(SqlQuestionSolver::solveQuestion)
                .orElse("SELECT 1;");
    }
    
    private Submission createSubmission(AssignmentType assignmentType, String finalQuery) {
        Submission submission = new Submission();
        submission.setRegNo(candidateConfig.getRegNo());
        submission.setAssignmentType(assignmentType);
        submission.setFinalQuery(finalQuery);
        submission.setStatus(SubmissionStatus.CREATED);
        return submission;
    }
    
    private String maskString(String input) {
        if (input == null || input.length() <= 8) {
            return input;
        }
        return input.substring(0, 4) + "..." + input.substring(input.length() - 4);
    }
}
