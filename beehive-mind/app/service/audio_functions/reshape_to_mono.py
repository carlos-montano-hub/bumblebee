import numpy as np
from pydub import AudioSegment


def convert_to_mono(audio: AudioSegment) -> AudioSegment:
    if audio.channels > 1:
        samples = np.array(audio.get_array_of_samples())
        samples = samples.reshape((-1, audio.channels))
        samples = samples.mean(axis=1)
        audio = audio._spawn(samples.astype(audio.array_type).tobytes())
    return audio
