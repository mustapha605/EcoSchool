# ==============================================================================
# Script de lancement automatique des 7 microservices - Eco-Awareness for Schools
# ESPRIT AWD 2026-2027
# ==============================================================================

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  Lancement de la plateforme Eco-Awareness for Schools     " -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Cyan

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

# 1. School-Class Service (Port 8081)
Write-Host "[1/5] Lancement de MS1: school-class-service (Port 8081)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\school-class'; Write-Host '--- MS1 : SCHOOL-CLASS (:8081) ---' -ForegroundColor Cyan; .\mvnw.cmd spring-boot:run"

# 2. Challenge Service (Port 8082)
Write-Host "[2/5] Lancement de MS2: challenge-service (Port 8082)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\challenge'; Write-Host '--- MS2 : CHALLENGE (:8082) ---' -ForegroundColor Green; .\mvnw.cmd spring-boot:run"

# 3. Quiz Service (Port 8083)
Write-Host "[3/5] Lancement de MS3: quiz-service (Port 8083)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\quiz'; Write-Host '--- MS3 : QUIZ (:8083) ---' -ForegroundColor DarkYellow; .\mvnw.cmd spring-boot:run"

# 4. Scoring Service (Spring Boot, Port 8084)
Write-Host "[4/7] Lancement de MS4: scoring-service (Port 8084)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\scoring'; Write-Host '--- MS4 : SCORING (:8084) ---' -ForegroundColor Magenta; mvn spring-boot:run"

# 5. Notification Service (Spring Boot, Port 8085)
Write-Host "[5/7] Lancement de MS5: notification-service (Port 8085)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\notification'; Write-Host '--- MS5 : NOTIFICATION (:8085) ---' -ForegroundColor Red; mvn spring-boot:run"

# 6. User Service (Spring Boot + Keycloak, Port 8086)
Write-Host "[6/7] Lancement de MS6: user-service (Port 8086)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\user'; Write-Host '--- MS6 : USER (:8086) ---' -ForegroundColor Blue; mvn spring-boot:run"

# 7. Tracking Service (Node.js, Port 8087)
Write-Host "[7/7] Lancement de MS7: tracking-service (Port 8087)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectRoot\Backend\MicroServices\tracking'; Write-Host '--- MS7 : TRACKING (:8087) ---' -ForegroundColor Green; npm install; npm start"

Write-Host "Tous les terminaux ont ete lances !" -ForegroundColor Green
Write-Host "Accedez aux documentations Swagger :" -ForegroundColor Cyan
Write-Host " - MS1 School-Class : http://localhost:8081/swagger-ui.html"
Write-Host " - MS2 Challenge    : http://localhost:8082/swagger-ui.html"
Write-Host " - MS3 Quiz         : http://localhost:8083/swagger-ui.html"
Write-Host " - MS4 Scoring      : http://localhost:8084/swagger-ui.html"
Write-Host " - MS5 Notification : http://localhost:8085/swagger-ui.html"
Write-Host " - MS6 User         : http://localhost:8086/swagger-ui.html"
Write-Host " - MS7 Tracking     : http://localhost:8087/api/tracking/health"
