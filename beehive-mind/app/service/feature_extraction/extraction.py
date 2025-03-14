import numpy as np
from pydub import AudioSegment

from service.audio_functions.reshape_to_mono import convert_to_mono


def compute_zcr(audio: AudioSegment) -> float:
    samples = np.array(audio.get_array_of_samples())
    audio = convert_to_mono(audio)
    zero_crossings = np.sum(np.diff(np.sign(samples)) != 0)
    zcr = zero_crossings / len(samples)

    return zcr


def compute_energy(audio: AudioSegment) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    audio = convert_to_mono(audio)
    energy = np.mean(samples**2)

    return energy
