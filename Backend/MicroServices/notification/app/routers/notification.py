from fastapi import APIRouter
from typing import List
from datetime import datetime
from app.schemas import NotificationRequest, NotificationResponse

router = APIRouter()

@router.get("/hello")
def hello():
    return {"message": "hello I'm microservice notification"}

@router.get("/", response_model=List[NotificationResponse])
def get_notifications():
    return [
        NotificationResponse(
            id=1,
            recipient_id=101,
            title="Nouveau Défi : Zéro Plastique",
            message="Votre classe s'est inscrite au défi de la semaine !",
            is_read=False,
            created_at=datetime.now()
        )
    ]

@router.post("/", response_model=NotificationResponse, status_code=201)
def create_notification(payload: NotificationRequest):
    return NotificationResponse(
        id=2,
        recipient_id=payload.recipient_id,
        title=payload.title,
        message=payload.message,
        is_read=False,
        created_at=datetime.now()
    )
