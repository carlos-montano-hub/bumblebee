import numpy as np
import librosa
from pydub import AudioSegment

from service.audio_functions.reshape_to_mono import convert_to_mono


def extract_mfcc(audio: AudioSegment, n_mfcc: int = 13) -> np.ndarray:
    samples = np.array(audio.get_array_of_samples())
    # If the audio is stereo, reshape and average channels
    audio = convert_to_mono(audio)
    samples = samples.astype(np.float32)
    # Normalize the samples (assuming 16-bit audio)
    samples /= np.iinfo(np.int16).max
    sr = audio.frame_rate
    mfccs = librosa.feature.mfcc(y=samples, sr=sr, n_mfcc=n_mfcc)
    return mfccs
