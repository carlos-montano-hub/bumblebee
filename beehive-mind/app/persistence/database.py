from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from dotenv import load_dotenv
import os

from app.models.audio import Base

load_dotenv()

DB_URL = os.getenv("MIND_DB_URL")
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_PORT = os.getenv("DB_PORT", "5432")

DATABASE_URL = f"{DB_URL}:{DB_PORT}/{DB_NAME}"
DATABASE_URL = DATABASE_URL.replace("//", f"//{DB_USER}:{DB_PASSWORD}@")

engine = create_engine(DATABASE_URL)
Base.metadata.create_all(bind=engine)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False)
