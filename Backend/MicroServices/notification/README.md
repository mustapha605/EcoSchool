# notification microservice

Python / FastAPI microservice (**Technologie avancée**) handling eco-alerts, badge achievement notifications, and push notifications.

## Run
```bash
python -m venv venv
.\venv\Scripts\Activate.ps1
pip install -r requirements.txt
python -m uvicorn app.main:app --reload --port 8085
```
- API: http://localhost:8085/api/notifications/hello
- Swagger UI: http://localhost:8085/swagger-ui
- OpenAPI JSON: http://localhost:8085/v3/api-docs

Port: `8085`
