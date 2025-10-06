# Bumblebee Beehive Node Firmware

This repository contains the firmware for the **Bumblebee Beehive Node** project, including hardware abstraction components, middleware, and sample applications for fast hardware bringup.

---

## 📁 Folder Structure

```
.
├── apps/                   # Sample applications
│   ├── dht_sensor/         # Reads temperature and humidity from DHT sensors
│   ├── i2s_recorder/       # Records audio from PDM microphone to SD card
│   ├── sd_card_example/    # Simple SD card read/write tests
│   └── esp_http_client_example/  # HTTP client demo with certificates
├── components/             # Core reusable components
│   ├── node_sd/            # SD card wrapper
│   ├── node_i2s_pdm_mic/   # I2S microphone wrapper
│   └── node_wifi/          # Wi-Fi helper functions
├── middleware/             # Optional middleware and external dependencies
│   ├── node_dht/           # DHT sensor wrapper
│   └── dpp_easy_wifi_qr/   # QR provisioning middleware
├── managed_components/     # Third-party components managed by CMake
├── main/                   # Default app entry point (not used directly)
├── CMakeLists.txt          # Root CMake configuration
├── sdkconfig*              # ESP-IDF SDK configuration files
└── README.md               # This file
```

---

## ⚙️ Prerequisites

- Install the [ESP-IDF](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/get-started/index.html) framework.
- Make sure your environment is set up for your target hardware (ESP32/ESP32-S3, etc.).

---

## 🚀 Building & Selecting Applications

You can select which application to build by editing the `APP_NAME` variable in the root `CMakeLists.txt`:

```cmake
set(APP_NAME "esp_http_client_example" CACHE STRING "Select which application to build")
```

This will automatically point the build system to the corresponding folder in `apps/` and include any required middleware components.

---

## 🧩 Middleware / External Dependencies

You can specify additional middleware for each application using the variables defined in `CMakeLists.txt`:

```cmake
# Use separate variables per app
set(APP_COMPONENTS_dht_sensor "${CUSTOM_ROOT_PATH}/middleware/node_dht")
set(APP_COMPONENTS_i2s_recorder "")
set(APP_COMPONENTS_sd_card_example "")
set(APP_COMPONENTS_esp_http_client_example "${CUSTOM_ROOT_PATH}/middleware/dpp_easy_wifi_qr")
```

These variables tell CMake which external components to include for each app during build time.

---

## 🛠️ Configuration

You can run the following command to open the **menuconfig** interface, where you can configure build options, component settings, and hardware parameters:

```bash
idf.py menuconfig
```

Use this to fine-tune Wi-Fi credentials, logging levels, pin assignments, and other options provided by ESP-IDF and your components.

---

## 🧠 Notes

- Each app is independent and can be built without affecting others.
- Middleware and components provide a consistent API for interacting with hardware.
- Sample apps are designed for fast hardware verification and bringup.
- To add a new app:
  1. Create a new folder in `apps/`.
  2. Add any required components to `middleware/`.
  3. Update `CMakeLists.txt` to include the new app in `APP_NAME` selection and define its dependencies.

---

## 📄 License

[Add your license information here, e.g., MIT License]

---

© 2025 Bumblebee Systems. All rights reserved.
