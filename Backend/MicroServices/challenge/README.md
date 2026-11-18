# challenge microservice

Spring Boot 3.2.5 / Java 17 service managing **Eco-challenges**, **Proof Submissions**, and **Teacher Validations**.

## Eureka discovery client

This microservice registers itself as `challenge-service` with a separately running Eureka server.
The default server URL is `http://localhost:8761/eureka/`; set
`EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` to override it (for example, when the service runs in Docker).
Eureka is enabled only in this microservice; no other services or application-wide configuration are changed.

## Run
```bash
mvn spring-boot:run
```
- Port: `8082`
- Swagger UI: http://localhost:8082/swagger-ui.html
- OpenAPI Docs: http://localhost:8082/v3/api-docs

## Endpoints
| Method | Path | Description |
|---|---|---|
| GET | /api/challenges/hello | Health check |
| GET | /api/challenges | List active challenges |
| POST | /api/challenges | Create a challenge |
| GET | /api/challenges/{id} | Get challenge details |
