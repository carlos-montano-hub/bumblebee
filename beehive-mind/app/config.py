from pydantic_settings import BaseSettings
from pydantic import ConfigDict


class Settings(BaseSettings):
    app_name: str = "My FastAPI App"
    debug: bool = False

    model_config = ConfigDict(
        env_file="../.env",
        host="0.0.0.0",
        port=8853,
    )


settings = Settings()
