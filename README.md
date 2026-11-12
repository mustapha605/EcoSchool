# Eco-Awareness for Schools — Microservices

Plateforme éducative qui gamifie l’éco-responsabilité en milieu scolaire et mesure l’impact réel des classes.

## Architecture

Chaque microservice a sa responsabilité et sa base de données. Les échanges asynchrones passent par l’exchange RabbitMQ durable `eco.events`.

| Service | Technologie | Port | Rôle |
|---|---:|---:|---|
| API Gateway | Spring Boot | 8080 | Point d’entrée unique |
| school-class-service | Spring Boot | 8081 | Écoles, classes et élèves |
| challenge-service | Spring Boot | 8082 | Défis et soumissions |
| quiz-service | Spring Boot | 8083 | Quiz et contenu éducatif |
| scoring-service | Spring Boot | 8084 | Scores et classements, consommateur RabbitMQ |
| notification-service | Spring Boot | 8085 | Notifications, consommateur RabbitMQ |
| user-service | Spring Boot + Keycloak | 8086 | Profil de l’utilisateur connecté et validation JWT |
| tracking-service | Node.js | 8087 | Actions mesurables : déchets, énergie, eau |
| Keycloak | Keycloak 24 | 8180 | Authentification, rôles PUPIL / TEACHER / SCHOOL_ADMIN |
| RabbitMQ management | RabbitMQ | 15672 | Supervision du broker |

## Liens locaux

- [Gateway](http://localhost:8080)
- [School & Class Swagger](http://localhost:8081/swagger-ui.html)
- [Challenge Swagger](http://localhost:8082/swagger-ui.html)
- [Quiz Swagger](http://localhost:8083/swagger-ui.html)
- [Scoring Swagger](http://localhost:8084/swagger-ui.html)
- [Notification Swagger](http://localhost:8085/swagger-ui.html)
- [User Swagger](http://localhost:8086/swagger-ui.html)
- [Tracking health](http://localhost:8087/api/tracking/health)
- [Keycloak console](http://localhost:8180) — `admin` / `admin`
- [RabbitMQ console](http://localhost:15672) — `guest` / `guest`

## Authentication

`user-service` is a Spring Boot OAuth2 Resource Server. Keycloak imports the `eco-school` realm automatically through Docker Compose. Obtain a Keycloak access token and call `GET /api/users/me` with `Authorization: Bearer <token>`.

## Event flow

`tracking-service` stores each action then publishes `tracking.action.recorded` in `eco.events`. Scoring and notification each own a durable queue (`scoring.events`, `notification.events`), so an environmental action updates the objective score and remains auditable. The other Spring Boot services are connected to RabbitMQ and can publish their domain events to the same exchange.

Example action:

```http
POST http://localhost:8087/api/tracking/actions
Content-Type: application/json

{ "classId": "6A", "metric": "WASTE_KG", "value": 12.5 }
```

Available metrics: `WASTE_KG`, `ENERGY_KWH`, `WATER_LITERS`. Consult `GET /api/tracking/classes/{classId}/summary` for totals.

## Start with Docker

```bash
docker compose up --build
```

To run individual services locally, use `mvn spring-boot:run` in each Spring Boot service folder; use `npm install && npm start` in `Backend/MicroServices/tracking`.
