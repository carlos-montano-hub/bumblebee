import os
import tempfile
import warnings
from pathlib import Path

import numpy as np
from df.enhance import enhance, init_df, load_audio
from loguru import logger
from pydub import AudioSegment


def reduce_noise_deep_filter(
    audio_segment: AudioSegment, noise_reduction_factor: float = 1.0
):
    # Initialize DeepFilterNet model
    model, df_state, _ = init_df()

    # Convert AudioSegment to numpy array
    samples = np.array(audio_segment.get_array_of_samples()).astype(np.float32)
    samples /= np.finfo(samples.dtype).max  # Normalize to [-1, 1]

    # Create a temporary file to store the audio
    with tempfile.NamedTemporaryFile(suffix=".wav", delete=False) as temp_file:
        temp_filename = temp_file.name
        audio_segment.export(temp_filename, format="wav")

    try:
        # Load audio using DeepFilterNet's load_audio function
        audio, _ = load_audio(temp_filename, sr=df_state.sr())

        # Enhance (denoise) the audio
        enhanced = enhance(model, df_state, audio)

        # Apply noise reduction factor
        if noise_reduction_factor != 1.0:
            enhanced = audio + noise_reduction_factor * (enhanced - audio)

        # Convert back to int16 for AudioSegment
        enhanced_int16 = np.int16(enhanced * 32767)

        # Create a new AudioSegment from the enhanced audio
        return AudioSegment(
            enhanced_int16.tobytes(),
            frame_rate=df_state.sr(),
            sample_width=2,
            channels=1,
        )
    finally:
        # Clean up the temporary file
        os.unlink(temp_filename)
