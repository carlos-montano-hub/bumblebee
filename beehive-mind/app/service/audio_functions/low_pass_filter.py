from pydub import AudioSegment


def apply_low_pass_filter(
    audio: AudioSegment, cutoff_frequency: int = 750
) -> AudioSegment:
    return audio.low_pass_filter(cutoff_frequency)
