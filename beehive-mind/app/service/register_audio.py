from pydub import AudioSegment
from models.audio import Feature
from models.audio import Measurement
from sqlalchemy.orm import Session
import uuid
from datetime import date
from service.feature_extraction.extraction import (
    compute_zcr,
    compute_energy,
    compute_energy_entropy,
    compute_spectral_centroid,
    compute_spectral_spread,
    compute_spectral_entropy,
    compute_spectral_flux,
    compute_spectral_rolloff,
    compute_mfcc,
)


def register_audio(audio: AudioSegment, measurement_uuid: str, date: date) -> None:
    zero_crossing_rate = compute_zcr(audio)
    energy = compute_energy(audio)
    energy_entropy = compute_energy_entropy(audio)
    spectral_centroid = compute_spectral_centroid(audio, audio.frame_rate)
    spectral_spread = compute_spectral_spread(audio, audio.frame_rate)
    spectral_entropy = compute_spectral_entropy(audio)
    spectral_flux = compute_spectral_flux(audio)
    spectral_rolloff = compute_spectral_rolloff(audio, audio.frame_rate)
    mfcc = compute_mfcc(audio)

    session = Session()

    new_measurement = Measurement(measurement_uuid=measurement_uuid, date=date.today())

    session.add(new_measurement)
    session.commit()

    measurement_id = new_measurement.id

    audio_features = Feature(
        measurement_id=measurement_id,
        zero_crossing_rate=zero_crossing_rate,
        energy=energy,
        energy_entropy=energy_entropy,
        spectral_centroid=spectral_centroid,
        spectral_spread=spectral_spread,
        spectral_entropy=spectral_entropy,
        spectral_flux=spectral_flux,
        spectral_rolloff=spectral_rolloff,
        mfcc_1=mfcc[0],
        mfcc_2=mfcc[1],
        mfcc_3=mfcc[2],
        mfcc_4=mfcc[3],
        mfcc_5=mfcc[4],
        mfcc_6=mfcc[5],
        mfcc_7=mfcc[6],
        mfcc_8=mfcc[7],
        mfcc_9=mfcc[8],
        mfcc_10=mfcc[9],
        mfcc_11=mfcc[10],
        mfcc_12=mfcc[11],
        mfcc_13=mfcc[12],
    )

    audio_features.save()
