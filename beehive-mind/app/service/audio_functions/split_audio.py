from typing import List
from pydub import AudioSegment


def split_audio_file(audio: AudioSegment, segment_length=1000):
    try:
        segments: List[AudioSegment] = []
        for i in range(0, len(audio), segment_length):  # range(start, stop, step)
            segment = audio[
                i : i + segment_length
            ]  # Slicing the audio segment from i to i + segment_length
            segments.append(segment)
        return segments
    except Exception as e:
        print(f"Error splitting: {str(e)}")
        return []
