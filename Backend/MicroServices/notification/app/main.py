from fastapi import FastAPI
from app.routers import notification

app = FastAPI(
    title="Notification Microservice API",
    description="Python / FastAPI microservice sending eco-alerts, achievement notifications, and weekly school summaries",
    version="1.0.0",
    docs_url="/swagger-ui",
    openapi_url="/v3/api-docs"
)

app.include_router(notification.router, prefix="/api/notifications", tags=["Notifications"])

@app.get("/")
def root():
    return {"status": "notification service is running"}
