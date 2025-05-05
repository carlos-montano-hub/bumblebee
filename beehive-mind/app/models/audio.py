from datetime import date
from sqlalchemy import Column, BigInteger, Date, UUID, ForeignKey, Double, String
from sqlalchemy.ext.declarative import declarative_base
from pydantic import BaseModel


class RegisterAudioForm(BaseModel):
    audioId: str
    date: date
    label: str | None = None


class ClassificationResult(BaseModel):
    label: str
    confidence: float


Base = declarative_base()


class Feature(Base):
    __tablename__ = "features"
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    measurement_id = Column(UUID, nullable=False)
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
    label = Column(String(50), nullable=True)

    def as_feature_list(self):
        return [
            self.zero_crossing_rate,
            self.energy,
            self.energy_entropy,
            self.spectral_centroid,
            self.spectral_spread,
            self.spectral_entropy,
            self.spectral_flux,
            self.spectral_rolloff,
            self.mfcc_1,
            self.mfcc_2,
            self.mfcc_3,
            self.mfcc_4,
            self.mfcc_5,
            self.mfcc_6,
            self.mfcc_7,
            self.mfcc_8,
            self.mfcc_9,
            self.mfcc_10,
            self.mfcc_11,
            self.mfcc_12,
            self.mfcc_13,
        ]

    def to_dict(self):
        """
        Convert a Feature object to a dictionary.

        Returns:
            dict: A dictionary representation of the Feature object.
        """
        return {
            "id": self.id,
            "measurement_id": str(self.measurement_id),
            "date": self.date.isoformat(),
            "zero_crossing_rate": self.zero_crossing_rate,
            "energy": self.energy,
            "energy_entropy": self.energy_entropy,
            "spectral_centroid": self.spectral_centroid,
            "spectral_spread": self.spectral_spread,
            "spectral_entropy": self.spectral_entropy,
            "spectral_flux": self.spectral_flux,
            "spectral_rolloff": self.spectral_rolloff,
            "mfcc_1": self.mfcc_1,
            "mfcc_2": self.mfcc_2,
            "mfcc_3": self.mfcc_3,
            "mfcc_4": self.mfcc_4,
            "mfcc_5": self.mfcc_5,
            "mfcc_6": self.mfcc_6,
            "mfcc_7": self.mfcc_7,
            "mfcc_8": self.mfcc_8,
            "mfcc_9": self.mfcc_9,
            "mfcc_10": self.mfcc_10,
            "mfcc_11": self.mfcc_11,
            "mfcc_12": self.mfcc_12,
            "mfcc_13": self.mfcc_13,
            "label": self.label,
        }
