from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_hello():
    response = client.get("/api/notifications/hello")
    assert response.status_code == 200
    assert response.json() == {"message": "hello I'm microservice notification"}
