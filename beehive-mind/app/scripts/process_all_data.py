import logging
import os
from pathlib import Path
import warnings
from pydub import AudioSegment
from loguru import logger
import soundfile as sf

from app.service.audio_functions.filter_silence import filter_audio_segments
from app.service.audio_functions.reduce_noise import reduce_noise_deep_filter
from app.service.audio_functions.split_audio import split_audio_file

# Set ffmpeg and ffprobe paths
AudioSegment.ffmpeg = r"C:\ProgramData\chocolatey\bin\ffmpeg.exe"
AudioSegment.ffprobe = r"C:\ProgramData\chocolatey\bin\ffprobe.exe"


def split_audio_folder(
    input_folder, output_folder, segment_length=1000, min_amplitude_db=-30
):
    # Set logging level to ERROR to suppress info logs
    logger.remove()
    logger.add(lambda msg: None, level="ERROR")

    # Filter out specific warnings
    warnings.filterwarnings("ignore", category=UserWarning)
    warnings.filterwarnings("ignore", category=FutureWarning)

    logging.getLogger("DF").setLevel(logging.ERROR)

    input_folder_path = Path(input_folder)
    output_folder_path = Path(output_folder)
    file_counter = 0
    file_skipped = 0
    file_processed = 0
    for file in input_folder_path.rglob("*"):
        if file.is_file() and file.suffix.lower() in {".wav", ".mp3"}:
            file_counter += 1
            try:
                # Create output subfolder matching the file's relative path.
                relative_path = file.relative_to(input_folder_path)
                output_subfolder = output_folder_path / relative_path.parent
                output_subfolder.mkdir(parents=True, exist_ok=True)

                # Load and process audio using PySoundFile
                data, samplerate = sf.read(str(file))
                temp_wav_path = os.path.join(output_subfolder, "temp.wav")
                sf.write(temp_wav_path, data, samplerate)

                audio = AudioSegment.from_file(temp_wav_path, format="wav")
                audio = reduce_noise_deep_filter(audio, noise_reduction_factor=0.5)

                # Split into segments and filter them.
                segments = split_audio_file(audio, segment_length)
                valid_segments = filter_audio_segments(segments, min_amplitude_db)

                for i, segment in enumerate(valid_segments):
                    output_file = (
                        output_folder_path
                        / f"{relative_path.parent.name}_{file_counter}_{i}.wav"
                    )
                    segment.export(output_file, format="wav")
                    print(f"Exported: {output_file}")
                    file_processed += 1

                print(f"Processed: {relative_path}")
            except Exception as e:
                print(f"Error processing {file}: {str(e)}")
                file_skipped += 1

    print("Finished")
    print(f"Processed {file_processed} segments")
    print(f"Skipped {file_skipped} files")


if __name__ == "__main__":
    # Set logging level to ERROR to suppress info logs
    logger.remove()
    logger.add(lambda msg: None, level="ERROR")

    # Filter out specific warnings
    warnings.filterwarnings("ignore", category=UserWarning)
    warnings.filterwarnings("ignore", category=FutureWarning)

    logging.getLogger("DF").setLevel(logging.ERROR)
    input_path = Path(
        "F:/Biblioteca/Maestria en CC/Tesis/Implementacion/Bumblebee/beehive-mind/data/audio_input"
    )
    output_path = Path(
        "F:/Biblioteca/Maestria en CC/Tesis/Implementacion/Bumblebee/beehive-mind/data/audio_output"
    )
    split_audio_folder(input_path, output_path)
