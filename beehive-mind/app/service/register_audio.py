import numpy as np
import librosa
from pydub import AudioSegment
from app.models.audio import Feature
from datetime import date
from app.service.feature_extraction.extraction import (
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
from app.persistence.database import SessionLocal


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

    zcr_frames = []
    energy_frames = []
    energy_entropy_frames = []
    spectral_centroid_frames = []
    spectral_spread_frames = []
    spectral_entropy_frames = []
    spectral_flux_frames = []
    spectral_rolloff_frames = []

    for frame in frames.T:
        audio_frame = AudioSegment(
            frame.tobytes(),
            frame_rate=sr,
            sample_width=audio.sample_width,
            channels=audio.channels,
        )
        zcr_frames.append(compute_zcr(audio_frame))
        energy_frames.append(compute_energy(audio_frame))
        energy_entropy_frames.append(compute_energy_entropy(audio_frame))
        spectral_centroid_frames.append(compute_spectral_centroid(audio_frame, sr))
        spectral_spread_frames.append(compute_spectral_spread(audio_frame, sr))
        spectral_entropy_frames.append(compute_spectral_entropy(audio_frame))
        spectral_flux_frames.append(compute_spectral_flux(audio_frame))
        spectral_rolloff_frames.append(compute_spectral_rolloff(audio_frame, sr))

    mfcc = compute_mfcc(
        audio
    )  # Uses     frame_length = 2048    hop_length = 512 by default
    session = SessionLocal()

    n_frames = mfcc.shape[1]
    for i in range(min(len(energy_entropy_frames), n_frames)):
        frame_feature = Feature(
            measurement_id=measurement_uuid,
            date=record_date,
            zero_crossing_rate=float(zcr_frames[i]),
            energy=float(energy_frames[i]),
            energy_entropy=float(energy_entropy_frames[i]),
            spectral_centroid=float(spectral_centroid_frames[i]),
            spectral_spread=float(spectral_spread_frames[i]),
            spectral_entropy=float(spectral_entropy_frames[i]),
            spectral_flux=float(spectral_flux_frames[i]),
            spectral_rolloff=float(spectral_rolloff_frames[i]),
            mfcc_1=float(mfcc[0, i]),
            mfcc_2=float(mfcc[1, i]),
            mfcc_3=float(mfcc[2, i]),
            mfcc_4=float(mfcc[3, i]),
            mfcc_5=float(mfcc[4, i]),
            mfcc_6=float(mfcc[5, i]),
            mfcc_7=float(mfcc[6, i]),
            mfcc_8=float(mfcc[7, i]),
            mfcc_9=float(mfcc[8, i]),
            mfcc_10=float(mfcc[9, i]),
            mfcc_11=float(mfcc[10, i]),
            mfcc_12=float(mfcc[11, i]),
            mfcc_13=float(mfcc[12, i]),
        )
        session.add(frame_feature)
    session.commit()
