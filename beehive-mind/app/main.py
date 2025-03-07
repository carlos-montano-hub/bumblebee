from fastapi import FastAPI
from config import settings
from routers import audios

app = FastAPI(title=settings.app_name)


app.include_router(audios.router)


@app.get("/", status_code=200)
async def health():
    return None
