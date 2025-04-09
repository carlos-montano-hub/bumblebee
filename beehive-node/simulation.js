const fs = require("fs");
const path = require("path");

const API_URL = `http://localhost:${process.env.NEST_SERVICE_PORT}/api/measure`;
const API_KEY = process.env.API_KEY;
const AUDIO_PATH = "../beehive-front/src/assets/audio/";
const AUDIO_NAME = "beehive_sound.wav";

const HEADERS = {
  Authorization: `Bearer ${API_KEY}`,
  "x-api-key": API_KEY,
};

async function sendData() {
  const data = {
    time: new Date().toISOString().replace("Z", ""),
    beehiveSerial: "SER001",
    temperature: 31.0 + (Math.random() * 3 - 1.5),
    humidity: 60.0 + (Math.random() * 30 - 10.0),
    weight: 10.5 + (Math.random() * 20 - 10.0),
  };

  try {
    const audioFilePath = path.join(AUDIO_PATH, AUDIO_NAME);
    const audioBuffer = fs.readFileSync(audioFilePath);
    const formData = new FormData();

    for (const [key, value] of Object.entries(data)) {
      formData.append(key, value.toString());
    }

    formData.append(
      "audioRecording",
      new Blob([audioBuffer], { type: "audio/wav" }),
      path.basename(audioFilePath)
    );

    const response = await fetch(API_URL, {
      method: "POST",
      body: formData,
      headers: HEADERS,
    });

    console.log(`\n Response: ${response.status}, ${await response.text()}`);
  } catch (error) {
    console.error("Error sending data:", error);
  }
}

setInterval(sendData, 2000);
