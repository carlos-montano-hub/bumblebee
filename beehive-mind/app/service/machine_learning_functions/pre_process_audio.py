from typing import List
from pydub import AudioSegment
import numpy as np

from service.audio_functions.filter_silence import filter_audio_segments
from service.audio_functions.low_pass_filter import apply_low_pass_filter
from service.audio_functions.reduce_noise import reduce_noise_deep_filter
from service.audio_functions.split_audio import split_audio_file


def get_full_preprocessing(audio: AudioSegment):
    """
    Applies a series of preprocessing steps to an audio segment:
      1. Low-pass filtering (default cutoff 750 Hz).
      2. Noise reduction via deep filtering.
      3. Splitting into 1000 ms segments.
      4. Filtering out segments below amplitude threshold.
      5. Merging remaining segments.
      6. Extracting MFCC features.

    Returns:
        np.ndarray: The extracted MFCC features.
    """

    audio = get_clean_audio(audio)

    audio_segments = split_audio_file(audio, segment_length=1000)
    audio_segments = filter_audio_segments(audio_segments)

    mfcc_list: List[np.ndarray] = []
    for i, segment in enumerate(audio_segments):
        mfcc_list.append(extract_mfcc(segment))

    return mfcc_list


def get_clean_audio(audio: AudioSegment):
    """
    Applies a series of preprocessing steps to an audio segment:
      1. Low-pass filtering (default cutoff 750 Hz).
      2. Noise reduction via deep filtering.

    Returns:
        audio: The cleaned audio.
    """
    audio = apply_low_pass_filter(audio)

    audio = reduce_noise_deep_filter(audio, 1)
    return audio
