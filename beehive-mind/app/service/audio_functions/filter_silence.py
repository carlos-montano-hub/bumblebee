from typing import List
from pydub import AudioSegment


def filter_audio_segments(segments: List[AudioSegment], min_amplitude_db=-30):
    filtered_segments: List[AudioSegment] = []
    for segment in segments:
        if segment.dBFS > min_amplitude_db:
            filtered_segments.append(segment)
    return filtered_segments
