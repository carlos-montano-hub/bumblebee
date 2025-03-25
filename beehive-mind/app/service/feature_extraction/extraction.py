import numpy as np
from pydub import AudioSegment
from scipy.fftpack import fft
from service.audio_functions.reshape_to_mono import convert_to_mono
import librosa


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


def compute_energy_entropy(audio: AudioSegment, num_bins: int = 10) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)

    audio = convert_to_mono(audio)

    energy = samples**2

    hist, _ = np.histogram(energy, bins=num_bins, density=True)

    entropy = -np.sum(hist * np.log2(hist))

    return entropy


def compute_spectral_centroid(audio: AudioSegment, sample_rate: int) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)

    if audio.channels > 1:
        samples = samples.reshape(-1, audio.channels).mean(axis=1)

    magnitude_spectrum = np.abs(fft(samples))
    frequencies = np.linspace(0, sample_rate / 2, len(magnitude_spectrum) // 2)

    spectral_centroid = np.sum(
        frequencies * magnitude_spectrum[: len(frequencies)]
    ) / np.sum(magnitude_spectrum[: len(frequencies)])

    return spectral_centroid


def compute_spectral_spread(audio: AudioSegment, sample_rate: int) -> float:
    centroid = compute_spectral_centroid(audio, sample_rate)
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    magnitude_spectrum = np.abs(fft(samples))
    frequencies = np.linspace(0, sample_rate / 2, len(magnitude_spectrum) // 2)

    spread = np.sqrt(
        np.sum(((frequencies - centroid) ** 2) * magnitude_spectrum[: len(frequencies)])
        / np.sum(magnitude_spectrum[: len(frequencies)])
    )
    return spread


def compute_spectral_entropy(audio: AudioSegment, num_bins: int = 10) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    magnitude_spectrum = np.abs(fft(samples))[: len(samples) // 2]

    hist, _ = np.histogram(magnitude_spectrum, bins=num_bins, density=True)
    hist += 1e-10

    entropy = -np.sum(hist * np.log2(hist))
    return entropy


def compute_spectral_flux(audio: AudioSegment) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    magnitude_spectrum = np.abs(fft(samples))

    flux = np.sum(np.diff(magnitude_spectrum) ** 2)
    return flux


def compute_spectral_rolloff(
    audio: AudioSegment, sample_rate: int, rolloff_percentage: float = 0.85
) -> float:
    samples = np.array(audio.get_array_of_samples(), dtype=np.float32)
    magnitude_spectrum = np.abs(fft(samples))[: len(samples) // 2]
    frequencies = np.linspace(0, sample_rate / 2, len(magnitude_spectrum))

    threshold = rolloff_percentage * np.sum(magnitude_spectrum)
    cumulative_sum = np.cumsum(magnitude_spectrum)
    rolloff = frequencies[np.nonzero(cumulative_sum >= threshold)[0][0]]
    return rolloff


def compute_mfcc(audio: AudioSegment, n_mfcc: int = 13) -> np.ndarray:
    samples = np.array(audio.get_array_of_samples())
    audio = convert_to_mono(audio)
    samples = samples.astype(np.float32)
    samples /= np.iinfo(np.int16).max
    sr = audio.frame_rate
    mfccs = librosa.feature.mfcc(y=samples, sr=sr, n_mfcc=n_mfcc)
    return mfccs
