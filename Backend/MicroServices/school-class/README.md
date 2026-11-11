# school-class microservice

Spring Boot 3.2.5 / Java 17 service managing **Schools**, **Classes**, and **Pupils** for the Eco-awareness platform.

## Run
```bash
mvn spring-boot:run
```
- Port: `8081`
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs
- H2 Console: http://localhost:8081/h2

## Endpoints
| Method | Path | Description |
|---|---|---|
| GET | /api/schools/hello | Health check |
| GET | /api/schools | List all schools |
| POST | /api/schools | Register a school |
| GET | /api/classes | List classes |
| GET | /api/classes/{id} | Get class details by ID |
