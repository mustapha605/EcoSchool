package com.awd.challenge.controller;

import com.awd.challenge.entity.Challenge;
import com.awd.challenge.entity.Submission;
import com.awd.challenge.repository.ChallengeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Challenges", description = "Endpoints for managing eco-challenges and submissions")
public class ChallengeController {

    private final ChallengeRepository challengeRepository;

    public ChallengeController(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @GetMapping("/challenges/hello")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of("message", "hello I'm microservice challenge"));
    }

    @GetMapping("/challenges")
    @Operation(summary = "List all eco-challenges")
    public List<Challenge> getChallenges() {
        return challengeRepository.findAll();
    }

    @PostMapping("/challenges")
    @Operation(summary = "Create an eco-challenge")
    public ResponseEntity<Challenge> createChallenge(@RequestBody Challenge challenge) {
        return new ResponseEntity<>(challengeRepository.save(challenge), HttpStatus.CREATED);
    }

    @GetMapping("/challenges/{id}")
    @Operation(summary = "Get challenge details by ID")
    public ResponseEntity<Challenge> getChallengeById(@PathVariable Long id) {
        return challengeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
