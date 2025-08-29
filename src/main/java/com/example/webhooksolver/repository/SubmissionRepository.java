package com.example.webhooksolver.repository;

import com.example.webhooksolver.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // Basic CRUD operations provided by JpaRepository
}
