# Scoring service

Service Spring Boot (port `8084`). Il consomme la file RabbitMQ `scoring.events` liée à `eco.events` et agrège les événements comportant `classId` et `points`.

- `GET /api/scoring/leaderboard`
- `GET /api/scoring/{classId}`
