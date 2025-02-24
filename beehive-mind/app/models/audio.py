from pydantic import BaseModel


class RegisterAudioForm(BaseModel):
    audioId: str
