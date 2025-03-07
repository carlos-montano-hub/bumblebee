# IoT Beehive Monitoring System "Bumblebee"

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A smart monitoring system for beehives using IoT technologies and machine learning, designed to help beekeepers optimize hive management.

## 📝 Abstract

Beekeeping is essential for honey production and plant pollination. In Sonora, Mexico, while only contributing 7% of national honey production, hives are primarily used for crop pollination. Traditional manual hive monitoring becomes inefficient as operations scale. This system proposes an IoT solution with sensors for temperature, humidity, weight, and audio analysis, combined with a mobile app for real-time monitoring and anomaly detection.

## 🛠️ System Architecture

### Backend Services

#### Java Catalog API (Spring Boot)

- Manages hive metadata and sensor data
- Integrates with audio storage service
- Database: PostgreSQL

#### Java Security API (Spring Boot)

- Handles user authentication/authorization
- Manages user permissions
- Database: PostgreSQL

#### Python ML API (FastAPI/Flask)

- Audio pattern recognition for anomaly detection
- Machine learning model integration
- Processes audio from static service

#### Static Audio Service

- Stores/retrieves audio files
- Unique ID system for audio recordings
- Integrated with catalog database

### IoT Components

#### MQTT Server

- Mosquitto or EMQX implementation
- Handles sensor data transmission

#### Microcontroller Unit

- Sensors: Temperature, Humidity, Weight, Audio
- MQTT client implementation
- Low-power operation

#### Mobile App

- Real-time data visualization
- Alert/notification system
- Cross-platform (Angular + capacitor)

## ⚙️ Installation

### Prerequisites

- Java 17+
- Python 3.10+
- PostgreSQL 14+
- MQTT Broker (Mosquitto/EMQX/AWS IOT)
- Angular 18+ (for the app)
- Rust (For the Audio functions)

### Setup Steps

1. Clone repository:
   ```bash
   git clone https://github.com/yourusername/beehive-monitoring-system.git
   ```
2. Configure databases:
   ```sql
    CREATE DATABASE beehive_nest;
    CREATE DATABASE beehive_guard;
   ```
3. Set up environment variables:
   ```bash
   JWT_SECRET=
   API_KEY=
   S3_ACCESS_KEY=
   S3_SECRET_KEY=
   GOOGLE_API_KEY=
   ```
4. Install Microsoft C++ Build Tools, Rust and deepfilternet

   - Install rust using chocolatey
     ```bash
     choco install rust
     ```
   - Download: [Build Tools for Visual Studio](https://visualstudio.microsoft.com/visual-cpp-build-tools/)
   - Install:
     - Select "C++ build tools" workload.
     - Include "MSVC v143 - VS 2022 C++ x64/x86 build tools" and "Windows 10 SDK".
   - Add cl.exe to path, usually in:

     `C:\Program Files (x86)\Microsoft Visual Studio\20XX\Community\VC\Tools\MSVC\XXX\bin\Hostx64\x64`

   - Or by running `cl` on `x64 Native Tools Command Prompt` (Search on Windows start menu)
   - Install deepfilternet using `x64 Native Tools Command Prompt`:
     ```bash
     pip install deepfilternet
     ```

5. Install dependencies for each service:

   ```bash
    # Java services
    cd beehive-guard && ./mvnw clean install
    cd beehive-nest && ./mvnw clean install

    # Python service
    cd beehive-mind && pip install -r requirements.txt
    choco install rust
    choco install ffmpeg
   ```

## 🚀 Usage

    Start MQTT broker

    Run Java APIs:
    bash
    Copy

    java -jar catalog-api/target/catalog-api-1.0.0.jar
    java -jar security-api/target/security-api-1.0.0.jar

    Start Python ML API:
    bash
    Copy

    uvicorn ml-api.main:app --reload

    Launch static audio service:
    bash
    Copy

    cd audio-service && npm start

    Configure microcontroller firmware

    Start mobile app development server

## 📧 Contact

- Name: Eng. Carlos Montaño
- Mail: montanoc70@gmail.com
- Phone: 6621438271
