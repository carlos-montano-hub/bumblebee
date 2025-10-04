#ifndef NODE_SD_H
#define NODE_SD_H

#include "esp_err.h"



/**
 * @brief Create a file on the SD card with a simple "Hello <card name>!" content.
 * 
 * @param file_name 
 */
esp_err_t node_sd_create_file(const char *file_name);

/**
 * @brief Write data to a file on the SD card.
 * 
 * @param path File path to write to (full path, max 64 chars)
 * @param data Data to write (max 64 chars)
 * @return esp_err_t 
 */
esp_err_t node_sd_write_file(const char *path, char *data);

/**
 * @brief Read data from a file on the SD card.
 * 
 * @param file_name File name to read from (max 64 chars)
 * @return esp_err_t General esp error code
 */
esp_err_t nose_sd_read_file(const char *path);

/**
 * @brief Initialize the SD card and mount the FAT filesystem.
 * 
 * Uses SPI peripheral to communicate with the SD card.
 * 
 */
void node_sd_init(void);


/**
 * @brief Deinitialize the SD card and unmount the FAT filesystem.
 * 
 */
void node_sd_deinit(void);

#endif // NODE_SD_H