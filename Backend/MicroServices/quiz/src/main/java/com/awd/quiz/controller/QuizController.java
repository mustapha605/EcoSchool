package com.awd.quiz.controller;

import com.awd.quiz.entity.Quiz;
import com.awd.quiz.repository.QuizRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Quizzes", description = "Endpoints for educational eco-quizzes")
public class QuizController {

    private final QuizRepository quizRepository;
    private final RabbitTemplate rabbitTemplate;

    public QuizController(QuizRepository quizRepository, RabbitTemplate rabbitTemplate) {
        this.quizRepository = quizRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/quizzes/hello")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of("message", "hello I'm microservice quiz"));
    }

    @GetMapping("/quizzes")
    @Operation(summary = "List all quizzes")
    public List<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }

    @PostMapping("/quizzes")
    @Operation(summary = "Create a quiz")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz saved = quizRepository.save(quiz);
        rabbitTemplate.convertAndSend("eco.events", "quiz.created", Map.of("quizId", saved.getId()));
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/quizzes/{id}")
    @Operation(summary = "Get quiz with questions")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
