from datetime import date
from sqlalchemy import Column, BigInteger, Date, UUID, ForeignKey, Double
from sqlalchemy.ext.declarative import declarative_base
from pydantic import BaseModel


class RegisterAudioForm(BaseModel):
    audioId: str
    date: date


Base = declarative_base()


class Feature(Base):
    __tablename__ = "features"
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    measurement_id = Column(UUID, unique=True, nullable=False)
    date = Column(Date, nullable=False)
    zero_crossing_rate = Column(Double)
    energy = Column(Double)
    energy_entropy = Column(Double)
    spectral_centroid = Column(Double)
    spectral_spread = Column(Double)
    spectral_entropy = Column(Double)
    spectral_flux = Column(Double)
    spectral_rolloff = Column(Double)
    mfcc_1 = Column(Double)
    mfcc_2 = Column(Double)
    mfcc_3 = Column(Double)
    mfcc_4 = Column(Double)
    mfcc_5 = Column(Double)
    mfcc_6 = Column(Double)
    mfcc_7 = Column(Double)
    mfcc_8 = Column(Double)
    mfcc_9 = Column(Double)
    mfcc_10 = Column(Double)
    mfcc_11 = Column(Double)
    mfcc_12 = Column(Double)
    mfcc_13 = Column(Double)
