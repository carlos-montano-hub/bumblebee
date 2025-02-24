import boto3
from botocore.exceptions import BotoCoreError, ClientError
from fastapi import APIRouter, HTTPException
from models.audio import RegisterAudioForm
from config import settings

router = APIRouter(prefix="/api/audio", tags=["Items"])


s3_client = boto3.client(
    "s3",
    aws_access_key_id=settings.s3_access_key,
    aws_secret_access_key=settings.s3_secret_key,
    region_name=settings.s3_region,
)


def get_file_from_s3(bucket_name: str, filename: str) -> bytes:
    try:
        response = s3_client.get_object(Bucket=bucket_name, Key=filename)
        return response["Body"].read()
    except ClientError as e:
        raise HTTPException(status_code=404, detail=f"File not found: {str(e)}")
    except BotoCoreError as e:
        raise HTTPException(status_code=500, detail=f"Error accessing S3: {str(e)}")


@router.post("/")
async def register(input: RegisterAudioForm):
    audio_id = input.audioId

    try:
        file_data = get_file_from_s3(settings.raw_bucket_name, audio_id)
        return {"message": "File retrieved successfully", "file_size": len(file_data)}
    except HTTPException as e:
        return {"error": e.detail}
