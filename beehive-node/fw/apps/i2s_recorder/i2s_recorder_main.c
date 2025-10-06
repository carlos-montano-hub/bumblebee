/*
 * SPDX-FileCopyrightText: 2021-2022 Espressif Systems (Shanghai) CO LTD
 *
 * SPDX-License-Identifier: Unlicense OR CC0-1.0
 */

/* I2S Digital Microphone Recording Example */
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sys/unistd.h>
#include <sys/stat.h>
#include "sdkconfig.h"
#include "esp_log.h"
#include "esp_err.h"
#include "esp_system.h"
#include "esp_vfs_fat.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "driver/i2s_pdm.h"
#include "driver/gpio.h"
#include "driver/spi_common.h"
#include "sdmmc_cmd.h"
#include "format_wav.h"

#include "node_i2s_pdm_mic.h"
#include "node_sd.h"

static const char *TAG = "pdm_rec_example";

#define NUM_CHANNELS        (1) // For mono recording only!
#define SAMPLE_SIZE         (CONFIG_EXAMPLE_BIT_SAMPLE * 1024)
#define BYTE_RATE           (CONFIG_EXAMPLE_SAMPLE_RATE * (CONFIG_EXAMPLE_BIT_SAMPLE / 8)) * NUM_CHANNELS


static int16_t i2s_readraw_buff[SAMPLE_SIZE];
size_t bytes_read;
const int WAVE_HEADER_SIZE = 44;



void record_wav(uint32_t rec_time)
{
    ESP_LOGI(TAG, "Starting WAV recording...");

    esp_err_t ret;
    node_sd_file_t file = {0};
    int flash_wr_size = 0;
    uint32_t flash_rec_time = BYTE_RATE * rec_time;

    const wav_header_t wav_header =
        WAV_HEADER_PCM_DEFAULT(flash_rec_time, 16, CONFIG_EXAMPLE_SAMPLE_RATE, 1);

    const char *file_path = SD_MOUNT_POINT "/record.wav";

    // Check if file exists and remove it if so
    struct stat st;
    if (stat(file_path, &st) == 0) {
        unlink(file_path);
        ESP_LOGI(TAG, "Existing file deleted: %s", file_path);
    }

    // Open a new file using the SD API
    ret = node_sd_open(&file, file_path, "w");
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to open file for writing: %s", file_path);
        return;
    }

    // Write the WAV header
    ret = node_sd_write(&file, &wav_header, sizeof(wav_header));
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to write WAV header");
        node_sd_close(&file);
        return;
    }

    // Start recording loop
    while (flash_wr_size < flash_rec_time) {
        // Read raw samples from I2S microphone
        if (node_i2s_pdm_mic_read((char *)i2s_readraw_buff, SAMPLE_SIZE, &bytes_read, 1000) == ESP_OK) {
            ESP_LOGD(TAG, "[0] %d [1] %d [2] %d [3] %d ...",
                     i2s_readraw_buff[0], i2s_readraw_buff[1],
                     i2s_readraw_buff[2], i2s_readraw_buff[3]);

            // Write samples to SD file
            if (node_sd_write(&file, i2s_readraw_buff, bytes_read) != ESP_OK) {
                ESP_LOGE(TAG, "Write failed at %d bytes", flash_wr_size);
                break;
            }

            flash_wr_size += bytes_read;
        } else {
            ESP_LOGW(TAG, "I2S read failed, continuing...");
        }
    }

    ESP_LOGI(TAG, "Recording done! Total bytes written: %d", flash_wr_size);

    node_sd_close(&file);
    ESP_LOGI(TAG, "WAV file saved successfully: %s", file_path);
}


void app_main(void)
{
    printf("--------------PDM microphone recording example start\n--------------------------------------\n");
    // Mount the SDCard for recording the audio file
    node_sd_init();
    // Acquire a I2S PDM channel for the PDM digital microphone
    node_i2s_pdm_mic_init();
    ESP_LOGI(TAG, "Starting recording for %d seconds!", CONFIG_EXAMPLE_REC_TIME);
    // Start Recording
    record_wav(CONFIG_EXAMPLE_REC_TIME);

    // All done, unmount partition and disable SPI peripheral
    node_sd_deinit();


    // Stop I2S driver and destroy
    node_i2s_pdm_mic_deinit();
    ESP_LOGI(TAG, "Application ended!");
}
