from fastapi import FastAPI
from config import settings
from routers import items

app = FastAPI(title=settings.app_name)


app.include_router(items.router)


@app.get("/")
async def root():
    return {"message": "Hello World", "debug_mode": settings.debug}
