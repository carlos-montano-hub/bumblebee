import os
import time
import random
import requests
from datetime import datetime

API_URL = f"{os.getenv('NEST_SERVICE_URL')}/api/measure"
API_KEY = os.getenv("API_KEY")
HEADERS = {
    "Authorization": f"Bearer {API_KEY}",
    "x-api-key": API_KEY,
}

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
AUDIO_DIR = os.path.join(SCRIPT_DIR, "../beehive-mind/data/audio_input")


def send_data(audio_path, label):
    data = {
        "time": datetime.now().isoformat(),
        "beehiveSerial": "SER001",
        "temperature": 31 + random.uniform(-1.5, 1.5),
        "humidity": 60 + random.uniform(-10, 20),
        "weight": 10.5 + random.uniform(-10, 10),
        "label": label,
    }
    with open(audio_path, "rb") as file:
        files = {"audioRecording": (os.path.basename(audio_path), file, "audio/wav")}
        response = requests.post(API_URL, data=data, files=files, headers=HEADERS)
        print(f"{os.path.basename(audio_path)} → {response.status_code} {label}")


def main():
    for entry in os.scandir(AUDIO_DIR):
        if entry.is_dir() and entry.name != "no_bee":
            label = "active" if entry.name == "active" else "anomaly"
            for fn in os.listdir(entry.path):
                if fn.lower().endswith((".wav")):
                    send_data(os.path.join(entry.path, fn), label)


if __name__ == "__main__":
    main()
