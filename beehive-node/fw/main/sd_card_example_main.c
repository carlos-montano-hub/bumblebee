/* SD card and FAT filesystem example.
   This example uses SPI peripheral to communicate with SD card.

   This example code is in the Public Domain (or CC0 licensed, at your option.)

   Unless required by applicable law or agreed to in writing, this
   software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied.
*/

#include <string.h>
#include <sys/unistd.h>
#include <sys/stat.h>
#include "esp_vfs_fat.h"
#include "sdmmc_cmd.h"

#if SOC_SDMMC_IO_POWER_EXTERNAL
#include "sd_pwr_ctrl_by_on_chip_ldo.h"
#endif
#include "node_sd.h"

#define EXAMPLE_MAX_CHAR_SIZE    64

static const char *TAG = "main";





void app_main(void)
{
    esp_err_t ret;

    node_sd_init();

    node_sd_create_file("hello.txt");
    node_sd_write_file("/sdcard/hello.txt", "Hello Beehive!");
    nose_sd_read_file("hello.txt");
    node_sd_deinit();
}
