from pydub import AudioSegment


def convert_to_mono(audio: AudioSegment) -> AudioSegment:
    if audio.channels > 1:
        samples = samples.reshape((-1, audio.channels))
        samples = samples.mean(axis=1)
    return audio
