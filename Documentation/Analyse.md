# Dossier d'Analyse et de Conception Microservices
## Projet AWD : Eco-Awareness for Schools (2026–2027)

---

## 1. Description du Sujet & Problème Réel

### 1.1 Contexte et Problématique
Les établissements scolaires (écoles primaires, collèges, lycées) représentent d'importants consommateurs d'énergie (éclairage, chauffage, climatisation) et générateurs de déchets (papier, plastique à usage unique, gaspillage à la cantine). Bien que la sensibilisation environnementale soit inscrite dans les programmes pédagogiques, elle reste souvent théorique et peine à motiver durablement les élèves.

### 1.2 Solution Proposée
**Eco-Awareness for Schools** est une plateforme distribuée qui ludifie l'action écologique au sein des classes via :
- Des **défis éco-responsables collectifs** (semaine zéro plastique, brigade éteignoir, pesée du compost).
- Des **quiz éducatifs interactifs** valorisant l'apprentissage écologique.
- Un **système de gamification** (points verts, badges de promotion, classements inter-classes).
- Des **alertes et bilans automatisés** pour informer les classes et directeurs d'école.

---

## 2. Acteurs du Système

| Acteur | Rôle & Permissions |
|---|---|
| **Élève (Pupil)** | Rejoint la classe, participe aux défis, soumet des preuves (photos, mesures), répond aux quiz, consulte ses scores et badges. |
| **Enseignant (Teacher)** | Enregistre sa classe, assigne des défis, valide les soumissions d'élèves, suit l'évolution des scores et organise les quiz. |
| **Directeur / Admin (School Admin)** | Supervise l'ensemble des classes de son établissement, accède aux métriques globales de réduction d'énergie/déchets. |
| **Système (Worker / Batch)** | Calcule les classements hebdomadaires, déclenche les notifications et distribue les badges automatiquement. |

---

## 3. User Stories par Microservice

### Microservice 1 : `school-class-service`
- **US1.1 (Enseignant)** : *En tant qu'enseignant*, je veux inscrire mon école et mes classes avec leur niveau scolaire, *afin de* structurer la participation de mes élèves.
- **US1.2 (Élève)** : *En tant qu'élève*, je veux m'inscrire dans ma classe via mon nom et email, *afin d'être* rattaché à une équipe collective.
- **US1.3 (Admin)** : *En tant que directeur*, je veux lister toutes les classes et le nombre d'élèves inscrits, *afin de* suivre le taux d'adoption de la plateforme.

### Microservice 2 : `challenge-service`
- **US2.1 (Enseignant)** : *En tant qu'enseignant*, je veux inscrire ma classe à un défi thématique (énergie, déchet, eau), *afin de* mobiliser les élèves sur un objectif mesurable.
- **US2.2 (Élève)** : *En tant qu'élève*, je veux soumettre une preuve de réalisation d'un éco-geste (photo, quantité économisée), *afin de* faire progresser ma classe.
- **US2.3 (Enseignant)** : *En tant qu'enseignant*, je veux valider ou rejeter les preuves soumises par mes élèves, *afin de* certifier l'effort écologique avant l'attribution des points.

### Microservice 3 : `quiz-service`
- **US3.1 (Élève)** : *En tant qu'élève*, je veux passer un quiz interactif hebdomadaire, *afin de* tester mes connaissances écologiques et gagner des points personnels.
- **US3.2 (Enseignant)** : *En tant qu'enseignant*, je veux créer de nouvelles questions et astuces écolos du jour, *afin d'enrichir* le contenu pédagogique.

### Microservice 4 : `scoring-service`
- **US4.1 (Élève / Classe)** : *En tant que membre d'une classe*, je veux visualiser le classement de mon école en temps réel, *afin de* stimuler une saine émulation entre classes.
- **US4.2 (Classe)** : *En tant que classe*, nous voulons débloquer des éco-badges (« Héros du Recyclage », « Sentinelle Énergie ») lorsque nous atteignons des paliers de points, *afin de* valoriser nos accomplissements.

### Microservice 5 : `notification-service` (Technologie Avancée - FastAPI)
- **US5.1 (Élève / Enseignant)** : *En tant qu'utilisateur*, je veux recevoir une notification instantanée lorsqu'un badge est débloqué ou qu'un défi est validé, *afin de* rester engagé.
- **US5.2 (Directeur)** : *En tant que directeur*, je veux recevoir un digest mensuel récapitulant les kWh et déchets évités, *afin d'intégrer* ces données au rapport RSE de l'école.

---

## 4. Données et Choix Tranché des Bases de Données

Conformément au principe fondamental **Database-per-Service**, aucune base n'est partagée.

| Microservice | Entités Métier Principales | Base de Données Choisie | Justification Tranchée (1 phrase) |
|---|---|---|---|
| **`school-class`** | `School`, `SchoolClass`, `Pupil`, `Teacher` | **MySQL 8** (ou H2 dev) | Choisi pour sa robustesse relationnelle garantissant l'intégrité référentielle stricte et les jointures ACID entre écoles, niveaux, classes et élèves. |
| **`challenge`** | `Challenge`, `Participation`, `Submission` | **PostgreSQL 16** (ou H2 dev) | Choisi pour son typage avancé, sa gestion performante des statuts d'état et le stockage semi-structuré JSON des métadonnées de preuves. |
| **`quiz`** | `Quiz`, `Question`, `AnswerOption`, `QuizAttempt`, `EcoTip` | **MongoDB** (ou H2 dev) | Choisi pour sa structure orientée documents sans schéma rigide, idéale pour des quiz polymorphes aux options et explications variables. |
| **`scoring`** | `ClassScore`, `PupilScore`, `Badge`, `ClassBadge` | **PostgreSQL 16** (+ Cache Redis) | Choisi pour sa rapidité d'agrégation numérique, ses fonctions de fenêtrage SQL (`DENSE_RANK()`) pour les classements, couplé à Redis pour le cache du leaderboard. |
| **`notification`** | `Notification`, `NotificationPreference` | **SQLite** (ou In-Memory) | Choisi pour son extrême légèreté embarquée sans serveur, parfaitement adaptée à un microservice asynchrone de dispatch d'alertes en Python FastAPI. |

---

## 5. Spécifications Exhaustives des APIs REST avec Codes de Statut

### 5.1 `school-class-service` (Port 8081)
| Méthode | URI | Description | Codes HTTP Prévus |
|---|---|---|---|
| `GET` | `/api/schools` | Liste paginée des écoles | `200 OK` |
| `POST` | `/api/schools` | Inscription d'une école | `201 Created`, `400 Bad Request`, `409 Conflict` (email/nom existant) |
| `GET` | `/api/schools/{id}` | Détail d'une école | `200 OK`, `404 Not Found` |
| `GET` | `/api/classes?schoolId={id}` | Classes d'un établissement | `200 OK`, `400 Bad Request` |
| `POST` | `/api/classes` | Création d'une classe | `201 Created`, `400 Bad Request`, `404 Not Found` (école inexistante) |
| `GET` | `/api/classes/{id}` | Détails d'une classe (appel OpenFeign) | `200 OK`, `404 Not Found` |
| `POST` | `/api/classes/{id}/pupils` | Inscription d'un élève dans la classe | `201 Created`, `400 Bad Request`, `409 Conflict` (email élève pris) |
| `GET` | `/api/pupils/{id}` | Fiche élève (appel OpenFeign) | `200 OK`, `404 Not Found` |

### 5.2 `challenge-service` (Port 8082)
| Méthode | URI | Description | Codes HTTP Prévus |
|---|---|---|---|
| `GET` | `/api/challenges` | Liste des défis actifs | `200 OK` |
| `POST` | `/api/challenges` | Création d'un éco-défi | `201 Created`, `400 Bad Request` |
| `GET` | `/api/challenges/{id}` | Détails d'un défi | `200 OK`, `404 Not Found` |
| `POST` | `/api/challenges/{id}/participate` | Inscription d'une classe au défi | `201 Created`, `404 Not Found` (défi ou classe absents), `409 Conflict` (déjà inscrit) |
| `POST` | `/api/participations/{id}/submissions` | Soumission de preuve par un élève | `201 Created`, `400 Bad Request`, `404 Not Found` |
| `PUT` | `/api/submissions/{id}/validate` | Validation par le professeur | `200 OK`, `404 Not Found`, `409 Conflict` (soumission déjà clôturée) |

### 5.3 `quiz-service` (Port 8083)
| Méthode | URI | Description | Codes HTTP Prévus |
|---|---|---|---|
| `GET` | `/api/quizzes` | Liste des quiz disponibles | `200 OK` |
| `POST` | `/api/quizzes` | Ajout d'un quiz avec questions | `201 Created`, `400 Bad Request` |
| `GET` | `/api/quizzes/{id}` | Quiz complet avec questions/options | `200 OK`, `404 Not Found` |
| `POST` | `/api/quizzes/{id}/submit` | Soumission des réponses élève | `200 OK` (retourne score & correction), `400 Bad Request`, `404 Not Found` |
| `GET` | `/api/tips/daily` | Astuce éco du jour | `200 OK`, `204 No Content` |

### 5.4 `scoring-service` (Port 8084)
| Méthode | URI | Description | Codes HTTP Prévus |
|---|---|---|---|
| `GET` | `/api/scoring/leaderboard` | Classement global des classes | `200 OK` |
| `GET` | `/api/scoring/classes/{classId}` | Score total et badges d'une classe | `200 OK`, `404 Not Found` |
| `POST` | `/api/scoring/points/add` | Ajout manuel ou interne de points | `200 OK`, `400 Bad Request`, `404 Not Found` |
| `GET` | `/api/scoring/badges` | Répertoire des badges disponibles | `200 OK` |

### 5.5 `notification-service` (Port 8085)
| Méthode | URI | Description | Codes HTTP Prévus |
|---|---|---|---|
| `GET` | `/api/notifications/hello` | Health check service | `200 OK` |
| `GET` | `/api/notifications?recipientId={id}` | Boîte de réception notifications | `200 OK` |
| `POST` | `/api/notifications` | Envoi d'une alerte ciblée | `201 Created`, `400 Bad Request` |
| `PATCH` | `/api/notifications/{id}/read` | Marquer une notification comme lue | `200 OK`, `404 Not Found` |

---

## 6. Tableau des Interactions Inter-services & Justifications

| Source | Cible | Mode | Données échangées | Justification métier |
|---|---|---|---|---|
| `challenge-service` | `school-class-service` | **Synchrone (OpenFeign)** | `classId` $\rightarrow$ `{ id, name, schoolId }` | Nécessité absolue de valider l'existence et l'éligibilité de la classe avant d'accepter une participation à un défi. |
| `quiz-service` | `school-class-service` | **Synchrone (OpenFeign)** | `pupilId` $\rightarrow$ `{ id, classId, active }` | Vérifie que l'élève appartient bien à une classe active avant de comptabiliser sa tentative de quiz. |
| `challenge-service` | `RabbitMQ` | **Asynchrone (Event)** | `ChallengeCompletedEvent` `{ challengeId, classId, points: 100, timestamp }` | Une validation de défi ne doit jamais être ralentie par le calcul des scores ni par l'envoi d'e-mails. |
| `quiz-service` | `RabbitMQ` | **Asynchrone (Event)** | `QuizPassedEvent` `{ quizId, pupilId, classId, score: 20 }` | Découplage complet : l'élève reçoit sa correction immédiatement, l'incrémentation du score s'effectue en tâche de fond. |
| `RabbitMQ` | `scoring-service` | **Asynchrone (Consumer)** | Consomme `ChallengeCompletedEvent` & `QuizPassedEvent` | Met à jour les points totaux, recalcule les rangs et vérifie l'attribution de nouveaux badges. |
| `scoring-service` | `RabbitMQ` | **Asynchrone (Event)** | `BadgeUnlockedEvent` `{ classId, badgeName, badgeIcon }` | Déclenche les alertes festives sans impacter le moteur de classement. |
| `RabbitMQ` | `notification-service` | **Asynchrone (Consumer)** | Consomme `ChallengeCompletedEvent` & `BadgeUnlockedEvent` | Envoi d'e-mails et de notifications push aux élèves et professeurs. |

---

## 7. Comportement Prévu en Cas de Panne (Résilience & Fault Tolerance)

Pour garantir la haute disponibilité requise par l'évaluation AWD, les mécanismes suivants sont conçus :

### 7.1 Pannes sur les Appels Synchrones (OpenFeign)
- **Scénario : `school-class-service` est indisponible lorsque `challenge-service` tente de vérifier une classe.**
  - **Mécanisme :** **Resilience4j Circuit Breaker & Fallback**.
  - **Comportement :**
    1. Après 3 échecs consécutifs ou un timeout de 2000 ms, le circuit breaker passe à l'état `OPEN`.
    2. Une méthode de Fallback locale est invoquée : elle consulte un cache local ou renvoie une réponse dégradée (`HTTP 503 Service Unavailable` avec code d'erreur applicatif `CLASS_SERVICE_UNAVAILABLE`).
    3. La transaction n'enregistre pas de données corrompues et invite l'utilisateur à réessayer ultérieurement sans bloquer les threads du serveur.

- **Scénario : `school-class-service` est indisponible pendant qu'un élève valide un quiz dans `quiz-service`.**
  - **Mécanisme :** **Retry Pattern (3 tentatives espacées de 500ms) + Fallback dégradé**.
  - **Comportement :** L'enregistrement du quiz est placé temporairement dans un buffer local en attente de réconciliation dès que le service annuaire est de retour.

### 7.2 Pannes sur les Flux Asynchrones (RabbitMQ)
- **Scénario : `scoring-service` crash ou est en maintenance lors de l'émission d'un `ChallengeCompletedEvent`.**
  - **Comportement :** Les messages sont persistés de manière durable dans la file d'attente RabbitMQ (`durable: true`). Aucune perte de données n'a lieu. Dès le redémarrage du conteneur `scoring-service`, les messages sont consommés dans l'ordre.
- **Scénario : Erreur de traitement d'un message corrompu (poison message).**
  - **Mécanisme :** **Dead Letter Queue (DLQ)** (`eco.events.dlq`).
  - **Comportement :** Après 3 tentatives infructueuses de désérialisation ou de traitement, le message est routé vers la file d'erreur DLQ avec les en-têtes d'exception, évitant le blocage de la file principale.

### 7.3 Panne de l'API Gateway ou du Service Discovery (Eureka)
- **Comportement :** Eureka dispose d'un cache client sur chaque microservice (`ribbon/loadbalancer cache`). Si le serveur Eureka tombe en panne, les microservices continuent de communiquer entre eux en utilisant leur dernière liste d'adresses mise en cache.
