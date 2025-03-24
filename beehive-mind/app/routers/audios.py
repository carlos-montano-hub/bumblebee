from fastapi import APIRouter, HTTPException
from app.models.audio import RegisterAudioForm
from app.config import settings
from app.service.api_functions import s3_functions

router = APIRouter(prefix="/api/audio", tags=["Items"])


@router.post("/")
async def register(input: RegisterAudioForm):
    audio_id = input.audioId

    try:
        file_data = s3_functions(settings.raw_bucket_name, audio_id)
        return {"message": "File retrieved successfully", "file_size": len(file_data)}
    except HTTPException as e:
        return {"error": e.detail}
