const fs = require("fs");
const path = require("path");
const fetch = require("node-fetch");
const FormData = require("form-data");

const API_URL = `${process.env.NEST_SERVICE_URL}/api/measure`;
const API_KEY = process.env.API_KEY;
const AUDIO_NAME = "beehive_sound.wav";

const HEADERS = {
  Authorization: `Bearer ${API_KEY}`,
  "x-api-key": API_KEY,
};

async function sendData(audioFilePath, label) {
  // 1) build your JSON fields
  const data = {
    time: new Date().toISOString().replace("Z", ""),
    beehiveSerial: "SER001",
    temperature: 31 + (Math.random() * 3 - 1.5),
    humidity: 60 + (Math.random() * 20 - 10),
    weight: 10.5 + (Math.random() * 20 - 10),
    label,
  };

  // 2) read the file and shove everything into a FormData instance
  const audioBuffer = fs.readFileSync(audioFilePath);
  const form = new FormData();
  Object.entries(data).forEach(([k, v]) => form.append(k, v.toString()));
  form.append("audioRecording", audioBuffer, path.basename(audioFilePath));

  // 3) merge in the multipart headers before sending
  const headers = {
    Authorization: `Bearer ${API_KEY}`,
    "x-api-key": API_KEY,
    ...form.getHeaders(), // ← critical to get boundary/content-type
  };

  console.log("Posting to:", API_URL, "\nHeaders:", headers);
  const res = await fetch(API_URL, { method: "POST", headers, body: form });
  console.log("Response:", res.status, await res.text());
}

const audioDir = path.resolve(__dirname, "../beehive-mind/data/audio_input");

async function processAudioFolders() {
  try {
    const folders = fs.readdirSync(audioDir, { withFileTypes: true });
    for (const dirent of folders) {
      if (shouldProcessDirectory(dirent)) {
        await processAudioFilesInFolder(dirent);
      }
    }
  } catch (err) {
    console.error("Error reading audio input directory:", err);
  }
}

function shouldProcessDirectory(dirent) {
  return dirent.isDirectory() && dirent.name !== "no_bee";
}

async function processAudioFilesInFolder(dirent) {
  const folderPath = path.join(audioDir, dirent.name);
  try {
    const files = fs.readdirSync(folderPath);
    for (const audioFile of files) {
      const isValidAudioFile =
        audioFile.endsWith(".wav") || audioFile.endsWith(".mp3");
      if (isValidAudioFile) {
        const audioFilePath = path.join(folderPath, audioFile);
        const label = dirent.name === "active" ? "active" : "anomaly";
        await sendData(audioFilePath, label);
      }
    }
  } catch (err) {
    console.error(`Error reading folder ${dirent.name}:`, err);
  }
}

processAudioFolders();
