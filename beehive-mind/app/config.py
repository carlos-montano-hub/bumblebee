from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "Beehive Mind App"
    debug: bool = True
    jwt_secret: str | None = None
    api_key: str | None = None
    s3_endpoint: str | None = None
    s3_access_key: str | None = None
    s3_secret_key: str | None = None
    s3_region: str = "us-east-1"
    audio_bucket_name: str | None = None
    google_api_key: str | None = None


settings = Settings()
