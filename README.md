# Eco-Awareness for Schools — Microservices Architecture

**Distributed Web Architecture (AWD)** · ESPRIT School of Engineering · 4th-year Computer Engineering · 2026–2027

---

## 1. Project Overview

**Eco-Awareness for Schools** is an educational microservices platform designed for primary and secondary schools to gamify eco-responsibility and track energy/waste reduction.
Through classroom challenges, interactive eco-quizzes, and green points competitions, students and classes unlock eco-badges and compete on school leaderboards.

### Target Actors & Roles
- **Pupils**: Participate in eco-challenges, submit proofs (waste reduction, energy savings), take educational quizzes, consult class scores and unlocked badges.
- **Teachers**: Register and manage classrooms, assign challenges, validate student submissions, monitor progress.
- **School Administrators**: Track school-wide ecological performance, view global leaderboards.

---

## 2. Microservices Architecture & Bounded Contexts

Following Domain-Driven Design (DDD) principles and the **Database-per-Service** pattern, each microservice possesses an isolated bounded context and its own database:

| Microservice | Bounded Context / Domain | Technology | Port | Database |
|---|---|---|---|---|
| **`school-class-service`** | Schools, Classrooms, Pupils & Teachers | Spring Boot 3.2.5 (Java 17) | `8081` | MySQL / H2 (`school_db`) |
| **`challenge-service`** | Challenges catalog, Class enrollments & Proof submissions | Spring Boot 3.2.5 (Java 17) | `8082` | PostgreSQL / H2 (`challenge_db`) |
| **`quiz-service`** | Eco-quizzes, Questions, Quiz attempts & Eco-tips | Spring Boot 3.2.5 (Java 17) | `8083` | MongoDB / H2 (`quiz_db`) |
| **`scoring-service`** | Eco-points aggregation, Badges, Class rankings | Node.js (Express) | `8084` | PostgreSQL / Redis (`scoring_db`) |
| **`notification-service`** | Instant alerts, Badge unlock notifications, Weekly digests | Python (FastAPI) — *Advanced Tech* | `8085` | SQLite / Stateless (`notif_db`) |

---

## 3. Communication Patterns

### Synchronous Communication (OpenFeign / REST)
Used when a service requires immediate validation from another bounded context:
- `challenge-service` $\rightarrow$ `school-class-service` (`GET /api/classes/{id}`): Verifies classroom existence prior to registering challenge participation.
- `quiz-service` $\rightarrow$ `school-class-service` (`GET /api/pupils/{id}`): Validates student enrollment before saving quiz results.

### Asynchronous Communication (RabbitMQ Event Broker)
Decoupled and resilient event flows via Topic Exchanges:
- **`ChallengeCompletedEvent`**: Published by `challenge-service` upon teacher validation $\rightarrow$ Consumed by `scoring-service` (+100 green points) and `notification-service`.
- **`QuizPassedEvent`**: Published by `quiz-service` $\rightarrow$ Consumed by `scoring-service` (+20 points).
- **`BadgeUnlockedEvent`**: Published by `scoring-service` $\rightarrow$ Consumed by `notification-service` to broadcast milestone achievements.

---

## 4. How to Launch the Microservices

Each microservice can be launched independently in its own terminal:

```bash
# 1. School & Class Service (Port 8081)
cd Backend/MicroServices/school-class
mvn spring-boot:run

# 2. Challenge Service (Port 8082)
cd Backend/MicroServices/challenge
mvn spring-boot:run

# 3. Quiz Service (Port 8083)
cd Backend/MicroServices/quiz
mvn spring-boot:run

# 4. Scoring Service (Port 8084)
cd Backend/MicroServices/scoring
npm install && npm start

# 5. Notification Service (Port 8085)
cd Backend/MicroServices/notification
python -m venv venv
.\venv\Scripts\Activate.ps1
pip install -r requirements.txt
python -m uvicorn app.main:app --reload --port 8085
```

---

## 5. Documentation & Diagrams

All architectural artifacts required for Validation 1 are located in `Documentation/`:
- `Documentation/Diagrammes/1-architecture-microservices.png`: Full technical architecture diagram (Gateway, Eureka, Keycloak, 5 Microservices, Databases, RabbitMQ, Docker).
- `Documentation/Diagrammes/2-diagramme-classe.png`: Conceptual class diagram colored by microservice ownership.
- `Documentation/Diagrammes/3-diagramme-classe-domaine.png`: Domain class diagram showing bounded contexts and ID-only cross-service references.
- `Documentation/Diagrammes/eco-awareness.drawio`: Editable Draw.io multi-page diagram source.
