import io
import os
import boto3
from botocore.exceptions import BotoCoreError, ClientError
from fastapi import HTTPException
from app.config import settings
from pydub import AudioSegment

s3_client = boto3.client(
    "s3",
    endpoint_url=settings.s3_endpoint,
    aws_access_key_id=settings.s3_access_key,
    aws_secret_access_key=settings.s3_secret_key,
    region_name=settings.s3_region,
)


def get_file_from_s3(bucket_name: str, filename: str) -> AudioSegment:
    try:
        response = s3_client.get_object(Bucket=bucket_name, Key=filename)
        audio_data = response["Body"].read()
        audio_segment = AudioSegment.from_file(io.BytesIO(audio_data))
        return audio_segment
    except ClientError as e:
        raise HTTPException(status_code=404, detail=f"File not found: {str(e)}")
    except BotoCoreError as e:
        raise HTTPException(status_code=500, detail=f"Error accessing S3: {str(e)}")


def put_file_to_s3(bucket_name: str, filename: str, audio: AudioSegment) -> None:
    try:
        buffer = io.BytesIO()
        audio.export(buffer, format="wav")
        buffer.seek(0)  # Reset buffer pointer to the beginning
        s3_client.upload_fileobj(buffer, bucket_name, filename)
    except ClientError as e:
        raise HTTPException(status_code=404, detail=f"File not found: {str(e)}")
    except BotoCoreError as e:
        raise HTTPException(status_code=500, detail=f"Error accessing S3: {str(e)}")
