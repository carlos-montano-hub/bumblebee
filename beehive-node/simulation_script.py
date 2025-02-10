import requests
import time
from datetime import datetime
import os

# API URL and Key
API_URL = "http://localhost:8080/api/measure"
API_KEY = os.getenv("API_KEY")  # Get the API key from environment variables

AUDIO_PATH = "./beehive-mind/data/audio_input/active/"
AUDIO_NAME = "CF003 - Active - Day - (214)-0-0.wav"

# Headers with API Key
HEADERS = {
    "Authorization": f"Bearer {API_KEY}",  # If using a Bearer token
    "x-api-key": API_KEY,  # Some APIs use a custom header like this
    "Content-Type": "multipart/form-data",
}

while True:
    data = {
        "time": datetime.now().isoformat(),
        "beehiveSerial": "SER001",
        "temperature": 25.0,
        "humidity": 60.0,
        "weight": 10.5,
    }

    with open(AUDIO_PATH + AUDIO_NAME, "rb") as audio_file:
        files = {"audioRecording": ("audio.wav", audio_file, "audio/wav")}
        response = requests.post(API_URL, data=data, files=files, headers=HEADERS)

    print(f"Response: {response.status_code}, {response.text}")
    time.sleep(2)  # Send data every 2 seconds
