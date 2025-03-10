from typing import List
from pydub import AudioSegment


def split_audio_file(audio: AudioSegment, segment_length=1000) -> List[AudioSegment]:
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
        raise e


def merge_audio_segments(segments: List[AudioSegment]) -> AudioSegment:
    try:
        merged_audio = AudioSegment.empty()
        for segment in segments:
            merged_audio += segment  # Append each segment sequentially.
        return merged_audio
    except Exception as e:
        print(f"Error merging segments: {str(e)}")
        raise e
