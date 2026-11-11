# quiz microservice

Spring Boot 3.2.5 / Java 17 service managing **Eco-quizzes**, **Questions**, and **Educational Tips**.

## Run
```bash
mvn spring-boot:run
```
- Port: `8083`
- Swagger UI: http://localhost:8083/swagger-ui.html
- OpenAPI Docs: http://localhost:8083/v3/api-docs

## Endpoints
| Method | Path | Description |
|---|---|---|
| GET | /api/quizzes/hello | Health check |
| GET | /api/quizzes | List quizzes |
| POST | /api/quizzes | Create a quiz |
| GET | /api/quizzes/{id} | Get quiz by ID |
