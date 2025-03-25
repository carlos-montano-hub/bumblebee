from pydantic_settings import BaseSettings
from pydantic import ConfigDict


class Settings(BaseSettings):
    app_name: str = "Beehive Mind App"
    debug: bool = True
    jwt_secret: str | None = None
    api_key: str | None = None
    s3_access_key: str | None = None
    s3_secret_key: str | None = None
    google_api_key: str | None = None
    s3_region: str = "us-east-1"
    raw_bucket_name = "raw-audios"

    model_config = ConfigDict(
        env_file="../.env",
        extra="forbid",
        host="0.0.0.0",
        port=8853,
    )


settings = Settings()
