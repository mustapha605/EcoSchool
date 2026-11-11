from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class NotificationRequest(BaseModel):
    recipient_id: int
    recipient_type: str # PUPIL, TEACHER, CLASS
    title: str
    message: str
    notification_type: str # CHALLENGE_INVITE, BADGE_UNLOCKED, WEEKLY_DIGEST

class NotificationResponse(BaseModel):
    id: int
    recipient_id: int
    title: str
    message: str
    is_read: bool = False
    created_at: datetime
