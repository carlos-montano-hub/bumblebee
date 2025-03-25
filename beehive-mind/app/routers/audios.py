from fastapi import APIRouter, HTTPException
from app.models.audio import RegisterAudioForm
from app.config import settings
from app.service.api_functions.s3_functions import get_file_from_s3
from app.service.register_audio import register_audio

router = APIRouter(prefix="/api/audio", tags=["Items"])


@router.post("/")
async def register(input: RegisterAudioForm):
    audio_id = input.audioId
    date = input.date

    try:
        file_data = get_file_from_s3(settings.raw_bucket_name, audio_id)
        return_data = {
            "message": "File retrieved successfully",
            "file_size": file_data.frame_count(),
        }
        print(return_data)
        register_audio(file_data, audio_id, date)
        return return_data
    except HTTPException as e:
        return {"error": e.detail}
