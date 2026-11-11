package com.awd.challenge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long challengeId;
    private Long classId;       // Reference ID to school-class microservice
    private Long pupilId;       // Reference ID to pupil
    private Long teacherId;     // Reference ID to validator teacher

    private String proofUrl;
    private String description;
    private Double metricSaved; // e.g. kWh saved or kg of plastic recycled
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private LocalDateTime submittedAt = LocalDateTime.now();

    public Submission() {}
    public Submission(Long challengeId, Long classId, Long pupilId, String proofUrl, String description) {
        this.challengeId = challengeId;
        this.classId = classId;
        this.pupilId = pupilId;
        this.proofUrl = proofUrl;
        this.description = description;
    }

    public Long getId() { return id; }
    public Long getChallengeId() { return challengeId; }
    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Long getPupilId() { return pupilId; }
    public void setPupilId(Long pupilId) { this.pupilId = pupilId; }
    public String getProofUrl() { return proofUrl; }
    public void setProofUrl(String proofUrl) { this.proofUrl = proofUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}
