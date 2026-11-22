# quiz microservice

Spring Boot 3.2.5 / Java 17 service managing **Eco-quizzes**, **Questions**, and **Educational Tips**.

- Port: `8083`
- Nom dans Eureka : `QUIZ-SERVICE` (`spring.application.name=quiz-service`)
- Swagger UI: http://localhost:8083/swagger-ui.html
- OpenAPI Docs: http://localhost:8083/v3/api-docs

## Endpoints
| Method | Path | Description |
|---|---|---|
| GET | /api/quizzes/hello | Health check |
| GET | /api/quizzes | List quizzes |
| POST | /api/quizzes | Create a quiz |
| GET | /api/quizzes/{id} | Get quiz by ID |

---

# Atelier Eureka + Observabilité (Actuator) appliqués à Quiz

## 1. Quiz comme client Eureka (atelier Eureka, partie 2)

| Étape | Fichier | Modification |
|---|---|---|
| 1 | `pom.xml` | BOM `spring-cloud-dependencies` 2023.0.4 + `spring-cloud-starter-netflix-eureka-client` |
| 2 | `QuizApplication.java` | `@EnableDiscoveryClient` (facultative, gardée pour expliciter le rôle) |
| 3 | `application.properties` | `eureka.client.service-url.defaultZone`, `register-with-eureka=true`, `fetch-registry=true`, `prefer-ip-address=true` |

`eureka.instance.instance-id=${spring.application.name}:${server.port}` donne un identifiant unique par instance (utile pour l'étape 4).
L'URL du serveur peut être surchargée par la variable d'environnement `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` (Docker).

**Ordre de démarrage** : RabbitMQ, puis `Infrastructure/eureka-server` (port 8761), puis Quiz.

```bash
cd Backend/Infrastructure/eureka-server && mvn spring-boot:run
cd Backend/MicroServices/quiz           && mvn spring-boot:run
```

Vérification : ouvrir http://localhost:8761, `QUIZ-SERVICE` doit apparaître `UP` en moins de 30 s.

### Lancer plusieurs instances (étape 4)
Quiz utilise une base H2 en mémoire : chaque instance a donc ses propres données.

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8093
```

Eureka affiche alors deux instances de `QUIZ-SERVICE` (`quiz-service:8083` et `quiz-service:8093`).

## 2. Observabilité avec Spring Boot Actuator

### Mise en place
- `pom.xml` : `spring-boot-starter-actuator`
- `application.properties` :
  - `management.endpoints.web.exposure.include=health,info,metrics,loggers`
  - `management.endpoint.health.show-details=always`
  - `eureka.client.healthcheck.enabled=true` : Eureka reçoit le vrai statut de `/health`
  - `management.info.env.enabled=true` + `info.app.*` : contenu de `/actuator/info`

### Endpoints à tester
| URL | Rôle |
|---|---|
| http://localhost:8083/actuator/health | Statut global + composants (`db`, `rabbit`, `diskSpace`, `ping`, `discoveryComposite`, `quizContent`) |
| http://localhost:8083/actuator/info | Nom, version, description, équipe |
| http://localhost:8083/actuator/metrics | Liste des métriques |
| http://localhost:8083/actuator/metrics/jvm.memory.used | Mémoire JVM utilisée |
| http://localhost:8083/actuator/metrics/http.server.requests | Requêtes HTTP (COUNT, TOTAL_TIME, MAX) |
| http://localhost:8083/actuator/loggers | Niveaux de logs |

Réponse attendue de `/actuator/health` :

```json
{
  "status": "UP",
  "components": {
    "db":                 { "status": "UP", "details": { "database": "H2" } },
    "diskSpace":          { "status": "UP" },
    "discoveryComposite": { "status": "UP" },
    "ping":               { "status": "UP" },
    "quizContent":        { "status": "UP", "details": { "quizzes": 0 } },
    "rabbit":             { "status": "UP" }
  }
}
```

> **Attention** : Quiz dépend de RabbitMQ (`spring-boot-starter-amqp`). Si RabbitMQ est arrêté, le composant `rabbit` passe DOWN, donc Quiz passe DOWN dans Eureka. C'est le comportement attendu de l'observabilité : démarrer RabbitMQ avant Quiz.

### Question : combien de requêtes HTTP Quiz a-t-il traitées ? Quelle est la plus lente ?
1. Appeler plusieurs fois `GET http://localhost:8083/api/quizzes`.
2. Ouvrir `GET /actuator/metrics/http.server.requests?tag=status:200` :
   - mesure **COUNT** = nombre de requêtes HTTP traitées avec succès ;
   - mesure **MAX** = durée (en secondes) de la requête la plus lente.

Réponse (à compléter avec vos valeurs) : COUNT = ___ ; MAX = ___ s.

### Provoquer une panne et la voir dans Eureka
Quiz utilise H2 en mémoire (pas de MySQL à arrêter). Deux façons de provoquer un DOWN :
- arrêter RabbitMQ : `/actuator/health` passe à `DOWN` (composant `rabbit`) et, dans le dashboard Eureka, `QUIZ-SERVICE` passe à **DOWN (1)** ;
- redémarrer RabbitMQ : Quiz repasse `UP`.

### Modifier le niveau de logs sans redémarrer (`/actuator/loggers`)
```bash
# Lire le niveau
curl http://localhost:8083/actuator/loggers/com.awd.quiz

# Passer le package en DEBUG à chaud
curl -X POST http://localhost:8083/actuator/loggers/com.awd.quiz \
     -H "Content-Type: application/json" -d '{"configuredLevel":"DEBUG"}'

# Revenir au niveau par défaut
curl -X POST http://localhost:8083/actuator/loggers/com.awd.quiz \
     -H "Content-Type: application/json" -d '{"configuredLevel":null}'
```

### HealthIndicator personnalisé
`com.awd.quiz.health.QuizContentHealthIndicator` : lit `QuizRepository.count()`.
Il apparaît dans `/actuator/health` sous `quizContent` (UP avec le nombre de quiz, DOWN avec l'erreur si la lecture échoue).

## Captures d'écran à ajouter (rendu attendu)
- Dashboard Eureka avec `QUIZ-SERVICE` (au moins 2 instances : 8083 et 8093)
- `/actuator/health` UP puis DOWN
- `/actuator/info`
- `/actuator/metrics/http.server.requests`
