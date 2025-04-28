from fastapi import APIRouter, HTTPException, BackgroundTasks
from app.models.audio import RegisterAudioForm, ClassificationResult
from app.config import settings
from app.service.api_functions.s3_functions import get_file_from_s3
from app.service.register_audio import register_audio

router = APIRouter(prefix="/api/audio", tags=["Items"])


@router.post("")
async def register(input: RegisterAudioForm, background_tasks: BackgroundTasks):
    audio_id = input.audioId
    print(input.date)
    date = input.date

    try:
        file_data = get_file_from_s3(settings.audio_bucket_name, audio_id)
        return_data = {
            "message": "File retrieved successfully",
            "file_size": file_data.frame_count(),
        }
        print(return_data)
        background_tasks.add_task(
            register_audio, file_data, audio_id, date, input.label
        )

        return ClassificationResult(label="test", confidence=1.0)
    except HTTPException as e:
        return {"error": e.detail}
