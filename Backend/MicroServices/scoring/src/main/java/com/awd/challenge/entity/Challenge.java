package com.awd.challenge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private String category; // ENERGY, WASTE, WATER, BIODIVERSITY
    private Integer targetPoints;
    private Integer durationDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status = "ACTIVE";

    public Challenge() {}
    public Challenge(String title, String description, String category, Integer targetPoints) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.targetPoints = targetPoints;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getTargetPoints() { return targetPoints; }
    public void setTargetPoints(Integer targetPoints) { this.targetPoints = targetPoints; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
