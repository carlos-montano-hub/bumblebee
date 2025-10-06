#include <stdio.h>
#include "node_sd.h"

#include <string.h>
#include <sys/unistd.h>
#include <sys/stat.h>
#include "esp_vfs_fat.h"
#include "sdmmc_cmd.h"
#if SOC_SDMMC_IO_POWER_EXTERNAL
#include "sd_pwr_ctrl_by_on_chip_ldo.h"
#endif

#define EXAMPLE_MAX_CHAR_SIZE    64

static const char *TAG = "node_sd";



static sdmmc_card_t *card = NULL;
static const char mount_point[] = SD_MOUNT_POINT;

// By default, SD card frequency is initialized to SDMMC_FREQ_DEFAULT (20MHz)
// For setting a specific frequency, use host.max_freq_khz (range 400kHz - 20MHz for SDSPI)
// Example: for fixed frequency of 10MHz, use host.max_freq_khz = 10000;
static sdmmc_host_t host = SDSPI_HOST_DEFAULT();

#ifdef CONFIG_NODE_SD_SPI_DEBUG_PIN_CONNECTIONS
const char* names[] = {"CLK ", "MOSI", "MISO", "CS  "};
const int pins[] = {CONFIG_NODE_SD_SPI_PIN_CLK,
                    CONFIG_NODE_SD_SPI_PIN_MOSI,
                    CONFIG_NODE_SD_SPI_PIN_MISO,
                    CONFIG_NODE_SD_SPI_PIN_CS};

const int pin_count = sizeof(pins)/sizeof(pins[0]);
#if CONFIG_NODE_SD_SPI_ENABLE_ADC_FEATURE
const int adc_channels[] = {CONFIG_NODE_SD_SPI_ADC_PIN_CLK,
                            CONFIG_NODE_SD_SPI_ADC_PIN_MOSI,
                            CONFIG_NODE_SD_SPI_ADC_PIN_MISO,
                            CONFIG_NODE_SD_SPI_ADC_PIN_CS};
#endif //CONFIG_NODE_SD_SPI_ENABLE_ADC_FEATURE

pin_configuration_t config = {
    .names = names,
    .pins = pins,
#if CONFIG_NODE_SD_SPI_ENABLE_ADC_FEATURE
    .adc_channels = adc_channels,
#endif
};
#endif //CONFIG_NODE_SD_SPI_DEBUG_PIN_CONNECTIONS

// Pin assignments can be set in menuconfig, see "SD SPI Example Configuration" menu.
// You can also change the pin assignments here by changing the following 4 lines.
#define PIN_NUM_MISO  CONFIG_NODE_SD_SPI_PIN_MISO
#define PIN_NUM_MOSI  CONFIG_NODE_SD_SPI_PIN_MOSI
#define PIN_NUM_CLK   CONFIG_NODE_SD_SPI_PIN_CLK
#define PIN_NUM_CS    CONFIG_NODE_SD_SPI_PIN_CS

// Use POSIX and C standard library functions to work with files.

// Helper functions

/**
 * @brief Validate a filename and build the full path for SD card operations.
 *
 * This function checks that the filename is not NULL, is not empty, and does not exceed 32 characters.
 * It then constructs the full file path using the SD_MOUNT_POINT and stores it in file_path_out.
 *
 * @param file_name         The name of the file (must be a valid, non-empty string, max 32 chars).
 * @param file_path_out     Output buffer to store the full file path.
 * @param file_path_out_size Size of the output buffer (should be at least 64 bytes).
 * @return esp_err_t        ESP_OK if valid and path built, ESP_FAIL otherwise.
 */
static esp_err_t node_sd_validate_and_build_path(const char *file_name, char *file_path_out, size_t file_path_out_size)
{
    if (file_name == NULL) {
        ESP_LOGE(TAG, "Filename is NULL!");
        return ESP_FAIL;
    }
    if (strlen(file_name) == 0 || strlen(file_name) > 32) {
        ESP_LOGE(TAG, "Filename length invalid!");
        return ESP_FAIL;
    }
    int n = snprintf(file_path_out, file_path_out_size, SD_MOUNT_POINT"/%s", file_name);
    if (n < 0 || (size_t)n >= file_path_out_size) {
        ESP_LOGE(TAG, "Filename too long for path buffer!");
        return ESP_FAIL;
    }
    return ESP_OK;
}

// Exposed API functions



esp_err_t node_sd_open(node_sd_file_t *file, const char *path, const char *mode)
{
    file->fp = fopen(path, mode);
    if (file->fp == NULL) return ESP_FAIL;
    strncpy(file->path, path, sizeof(file->path));
    return ESP_OK;
}

esp_err_t node_sd_write(node_sd_file_t *file, const void *data, size_t len)
{
    if (!file->fp) return ESP_FAIL;
    fwrite(data, 1, len, file->fp);
    return ESP_OK;
}

esp_err_t node_sd_close(node_sd_file_t *file)
{
    if (!file->fp) return ESP_FAIL;
    fclose(file->fp);
    file->fp = NULL;
    return ESP_OK;
}


esp_err_t node_sd_create_file(const char *file_name)
{
    char file_path[64];

    // Use the helper to validate and build the full path
    if (node_sd_validate_and_build_path(file_name, file_path, sizeof(file_path)) != ESP_OK) {
        return ESP_FAIL;
    }

    // Check if file exists
    struct stat st;
    if (stat(file_path, &st) == 0) {
        ESP_LOGE(TAG, "Tried to create an already existing file!");
        return ESP_FAIL;
    }

    // Create an empty file
    FILE *f = fopen(file_path, "w");if (f == NULL) {
        ESP_LOGE(TAG, "Failed to create file");
        return ESP_FAIL;
    }
    fclose(f);

    return ESP_OK;
}



esp_err_t node_sd_write_file(const char *path, char *data)
{
    ESP_LOGI(TAG, "Opening file %s", path);
    FILE *f = fopen(path, "w");
    if (f == NULL) {
        ESP_LOGE(TAG, "Failed to open file for writing");
        return ESP_FAIL;
    }
    fprintf(f, data);
    fclose(f);
    ESP_LOGI(TAG, "File written");

    return ESP_OK;
}


esp_err_t node_sd_read_file(const char *file_name)
{
    char file_path[64];
    // Use the helper to validate and build the full path
    if (node_sd_validate_and_build_path(file_name, file_path, sizeof(file_path)) != ESP_OK) {
        return ESP_FAIL;
    }

    ESP_LOGI(TAG, "Reading file %s", file_path);
    FILE *f = fopen(file_path, "r");
    if (f == NULL) {
        ESP_LOGE(TAG, "Failed to open file for reading");
        return ESP_FAIL;
    }
    char line[EXAMPLE_MAX_CHAR_SIZE];
    fgets(line, sizeof(line), f);
    fclose(f);

    // strip newline
    char *pos = strchr(line, '\n');
    if (pos) {
        *pos = '\0';
    }
    ESP_LOGI(TAG, "Read from file: '%s'", line);

    return ESP_OK;
}


void node_sd_init(void)
{
    esp_err_t ret;

    // Options for mounting the filesystem.
    // If format_if_mount_failed is set to true, SD card will be partitioned and
    // formatted in case when mounting fails.
    esp_vfs_fat_sdmmc_mount_config_t mount_config = {
#ifdef CONFIG_NODE_SD_SPI_FORMAT_IF_MOUNT_FAILED
        .format_if_mount_failed = true,
#else
        .format_if_mount_failed = false,
#endif // EXAMPLE_FORMAT_IF_MOUNT_FAILED
        .max_files = 5,
        .allocation_unit_size = 16 * 1024
    };
    
    
    ESP_LOGI(TAG, "Initializing SD card");

    // Use settings defined above to initialize SD card and mount FAT filesystem.
    // Note: esp_vfs_fat_sdmmc/sdspi_mount is all-in-one convenience functions.
    // Please check its source code and implement error recovery when developing
    // production applications.
    ESP_LOGI(TAG, "Using SPI peripheral");

    

    // For SoCs where the SD power can be supplied both via an internal or external (e.g. on-board LDO) power supply.
    // When using specific IO pins (which can be used for ultra high-speed SDMMC) to connect to the SD card
    // and the internal LDO power supply, we need to initialize the power supply first.
#if CONFIG_NODE_SD_SPI_SD_PWR_CTRL_LDO_INTERNAL_IO
    sd_pwr_ctrl_ldo_config_t ldo_config = {
        .ldo_chan_id = CONFIG_NODE_SD_SPI_SD_PWR_CTRL_LDO_IO_ID,
    };
    sd_pwr_ctrl_handle_t pwr_ctrl_handle = NULL;

    ret = sd_pwr_ctrl_new_on_chip_ldo(&ldo_config, &pwr_ctrl_handle);
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to create a new on-chip LDO power control driver");
        return;
    }
    host.pwr_ctrl_handle = pwr_ctrl_handle;
#endif

    spi_bus_config_t bus_cfg = {
        .mosi_io_num = PIN_NUM_MOSI,
        .miso_io_num = PIN_NUM_MISO,
        .sclk_io_num = PIN_NUM_CLK,
        .quadwp_io_num = -1,
        .quadhd_io_num = -1,
        .max_transfer_sz = 4000,
    };

    ret = spi_bus_initialize(host.slot, &bus_cfg, SDSPI_DEFAULT_DMA);
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to initialize bus.");
        return;
    }

    // This initializes the slot without card detect (CD) and write protect (WP) signals.
    // Modify slot_config.gpio_cd and slot_config.gpio_wp if your board has these signals.
    sdspi_device_config_t slot_config = SDSPI_DEVICE_CONFIG_DEFAULT();
    slot_config.gpio_cs = PIN_NUM_CS;
    slot_config.host_id = host.slot;

    ESP_LOGI(TAG, "Mounting filesystem");
    ret = esp_vfs_fat_sdspi_mount(mount_point, &host, &slot_config, &mount_config, &card);

    if (ret != ESP_OK) {
        if (ret == ESP_FAIL) {
            ESP_LOGE(TAG, "Failed to mount filesystem. "
                     "If you want the card to be formatted, set the CONFIG_NODE_SD_SPI_FORMAT_IF_MOUNT_FAILED menuconfig option.");
        } else {
            ESP_LOGE(TAG, "Failed to initialize the card (%s). "
                     "Make sure SD card lines have pull-up resistors in place.", esp_err_to_name(ret));
#ifdef CONFIG_NODE_SD_SPI_DEBUG_PIN_CONNECTIONS
            check_sd_card_pins(&config, pin_count);
#endif
        }
        return;
    }
    ESP_LOGI(TAG, "Filesystem mounted");

    // Card has been initialized, print its properties
    sdmmc_card_print_info(stdout, card);
}



void node_sd_deinit(void)
{
    // All done, unmount partition and disable SPI peripheral
    esp_vfs_fat_sdcard_unmount(mount_point, card);
    ESP_LOGI(TAG, "Card unmounted");

    //deinitialize the bus after all devices are removed
    spi_bus_free(host.slot);

    // Deinitialize the power control driver if it was used
#if CONFIG_NODE_SD_SPI_SD_PWR_CTRL_LDO_INTERNAL_IO
    ret = sd_pwr_ctrl_del_on_chip_ldo(pwr_ctrl_handle);
    if (ret != ESP_OK) {
        ESP_LOGE(TAG, "Failed to delete the on-chip LDO power control driver");
        return;
    }
#endif
}
