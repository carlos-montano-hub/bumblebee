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

    model, df_state, _ = init_df()

    samples = np.array(audio_segment.get_array_of_samples()).astype(np.float32)
    samples /= np.finfo(samples.dtype).max

    with tempfile.NamedTemporaryFile(suffix=".wav", delete=False) as temp_file:
        temp_filename = temp_file.name
        audio_segment.export(temp_filename, format="wav")

    try:

        audio, _ = load_audio(temp_filename, sr=df_state.sr())

        enhanced = enhance(model, df_state, audio)

        if not np.isclose(noise_reduction_factor, 1.0, rtol=1e-09, atol=1e-09):
            enhanced = audio + noise_reduction_factor * (enhanced - audio)

        enhanced_int16 = np.int16(enhanced * 32767)

        return AudioSegment(
            enhanced_int16.tobytes(),
            frame_rate=df_state.sr(),
            sample_width=2,
            channels=1,
        )
    finally:

        os.unlink(temp_filename)
