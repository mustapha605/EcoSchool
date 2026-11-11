# challenge microservice

Spring Boot 3.2.5 / Java 17 service managing **Eco-challenges**, **Proof Submissions**, and **Teacher Validations**.

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
