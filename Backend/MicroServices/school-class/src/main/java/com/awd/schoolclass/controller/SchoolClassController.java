package com.awd.schoolclass.controller;

import com.awd.schoolclass.entity.School;
import com.awd.schoolclass.entity.SchoolClass;
import com.awd.schoolclass.repository.SchoolClassRepository;
import com.awd.schoolclass.repository.SchoolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "School & Class", description = "Endpoints for managing schools and classrooms")
public class SchoolClassController {

    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository classRepository;

    public SchoolClassController(SchoolRepository schoolRepository, SchoolClassRepository classRepository) {
        this.schoolRepository = schoolRepository;
        this.classRepository = classRepository;
    }

    @GetMapping("/schools/hello")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of("message", "hello I'm microservice school-class"));
    }

    @GetMapping("/schools")
    @Operation(summary = "List all registered schools")
    public List<School> getSchools() {
        return schoolRepository.findAll();
    }

    @PostMapping("/schools")
    @Operation(summary = "Register a new school")
    public ResponseEntity<School> createSchool(@RequestBody School school) {
        return new ResponseEntity<>(schoolRepository.save(school), HttpStatus.CREATED);
    }

    @GetMapping("/classes")
    @Operation(summary = "List classes (optional filter by schoolId)")
    public List<SchoolClass> getClasses(@RequestParam(required = false) Long schoolId) {
        if (schoolId != null) {
            return classRepository.findBySchoolId(schoolId);
        }
        return classRepository.findAll();
    }

    @GetMapping("/classes/{id}")
    @Operation(summary = "Get classroom details by ID (Used synchronously by other services via OpenFeign)")
    public ResponseEntity<SchoolClass> getClassById(@PathVariable Long id) {
        return classRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
