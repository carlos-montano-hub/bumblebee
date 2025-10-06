#ifndef NODE_SD_H
#define NODE_SD_H

#include "esp_err.h"

#define SD_MOUNT_POINT "/sdcard"

typedef struct {
    FILE *fp;
    char  path[64];
} node_sd_file_t;



/**
 * @brief Open a file on the SD card.
 *
 * This function opens a file located on the SD card with the specified path and mode.
 * It initializes the provided node_sd_file_t structure with the file pointer and path.
 *
 * @param[out] file Pointer to a node_sd_file_t structure that will hold the file context.
 * @param[in] path Absolute or relative file path to open on the SD card.
 * @param[in] mode File open mode (e.g., "r", "w", "a", "r+", etc.), same as standard fopen().
 *
 * @return
 *  - ESP_OK: File opened successfully.
 *  - ESP_FAIL: Failed to open the file (e.g., invalid path or SD not mounted).
 *
 * @note The file must be closed using @ref node_sd_close after use to avoid resource leaks.
 */
esp_err_t node_sd_open(node_sd_file_t *file, const char *path, const char *mode);


/**
 * @brief Write binary data to an open SD file.
 *
 * Writes a specified number of bytes from the provided buffer into an open file.
 * The file must have been successfully opened beforehand using @ref node_sd_open.
 *
 * @param[in,out] file Pointer to an open SD file handle (node_sd_file_t).
 * @param[in] data Pointer to the buffer containing the data to write.
 * @param[in] len Number of bytes to write from the buffer.
 *
 * @return
 *  - ESP_OK: Data written successfully.
 *  - ESP_FAIL: The file handle is invalid or not open.
 *
 * @note This function does not perform an explicit flush. Call `fflush(file->fp)`
 *       if immediate data persistence is required.
 */
esp_err_t node_sd_write(node_sd_file_t *file, const void *data, size_t len);


/**
 * @brief Close an open file on the SD card.
 *
 * Closes a file previously opened with @ref node_sd_open and clears the file pointer
 * in the node_sd_file_t structure. Once closed, the handle must not be used again
 * unless reopened.
 *
 * @param[in,out] file Pointer to a node_sd_file_t structure representing an open file.
 *
 * @return
 *  - ESP_OK: File closed successfully.
 *  - ESP_FAIL: The file handle is invalid or not open.
 *
 * @note Always close files properly to ensure data integrity on the SD card.
 */
esp_err_t node_sd_close(node_sd_file_t *file);



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
esp_err_t node_sd_read_file(const char *path);

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