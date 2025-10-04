/* SD card and FAT filesystem example.
   This example uses SPI peripheral to communicate with SD card.

   This example code is in the Public Domain (or CC0 licensed, at your option.)

   Unless required by applicable law or agreed to in writing, this
   software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied.
*/

#include <string.h>
#include "node_sd.h"
#include "esp_log.h"

#define EXAMPLE_MAX_CHAR_SIZE    64

static const char *TAG = "main";





void app_main(void)
{
    esp_err_t ret;

    node_sd_init();

    ret = node_sd_create_file("hello.txt");
        if (ret != ESP_OK) {
            ESP_LOGE(TAG, "Failed to create file on SD card");
            return;
        }
    ret = node_sd_write_file("/sdcard/hello.txt", "Hello Beehive!");
        if (ret != ESP_OK) {
            ESP_LOGE(TAG, "Failed to write file on SD card");
            return;
        }
    ret = nose_sd_read_file("hello.txt");
        if (ret != ESP_OK) {
            ESP_LOGE(TAG, "Failed to read file from SD card");
            return;
        }
    node_sd_deinit();
}
