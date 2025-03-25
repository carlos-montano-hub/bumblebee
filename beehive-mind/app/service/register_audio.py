import numpy as np
import librosa
from pydub import AudioSegment
from models.audio import Feature
from sqlalchemy.orm import Session
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


def register_audio(
    audio: AudioSegment, measurement_uuid: str, record_date: date
) -> None:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    sr = audio.frame_rate

    frame_length = 2048
    hop_length = 512

    frames = librosa.util.frame(
        samples, frame_length=frame_length, hop_length=hop_length
    )

    zcr_frames = [compute_zcr(frame) for frame in frames.T]
    energy_frames = [compute_energy(frame) for frame in frames.T]
    energy_entropy_frames = [compute_energy_entropy(frame) for frame in frames.T]
    spectral_centroid_frames = [
        compute_spectral_centroid(frame, sr) for frame in frames.T
    ]
    spectral_spread_frames = [compute_spectral_spread(frame, sr) for frame in frames.T]
    spectral_entropy_frames = [compute_spectral_entropy(frame) for frame in frames.T]
    spectral_flux_frames = [compute_spectral_flux(frame) for frame in frames.T]
    spectral_rolloff_frames = [
        compute_spectral_rolloff(frame, sr) for frame in frames.T
    ]

    mfcc = compute_mfcc(
        audio
    )  # Uses     frame_length = 2048    hop_length = 512 by default
    session = Session()

    n_frames = mfcc.shape[1]
    for i in range(n_frames):
        frame_feature = Feature(
            measurement_id=measurement_uuid,
            date=record_date,
            frame_index=i,
            zero_crossing_rate=zcr_frames[i],
            energy=energy_frames[i],
            energy_entropy=energy_entropy_frames[i],
            spectral_centroid=spectral_centroid_frames[i],
            spectral_spread=spectral_spread_frames[i],
            spectral_entropy=spectral_entropy_frames[i],
            spectral_flux=spectral_flux_frames[i],
            spectral_rolloff=spectral_rolloff_frames[i],
            mfcc_1=mfcc[0, i],
            mfcc_2=mfcc[1, i],
            mfcc_3=mfcc[2, i],
            mfcc_4=mfcc[3, i],
            mfcc_5=mfcc[4, i],
            mfcc_6=mfcc[5, i],
            mfcc_7=mfcc[6, i],
            mfcc_8=mfcc[7, i],
            mfcc_9=mfcc[8, i],
            mfcc_10=mfcc[9, i],
            mfcc_11=mfcc[10, i],
            mfcc_12=mfcc[11, i],
            mfcc_13=mfcc[12, i],
        )
        print(frame_feature)
        session.add(frame_feature)
    session.commit()
