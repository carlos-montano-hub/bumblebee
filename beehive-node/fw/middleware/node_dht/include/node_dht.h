#ifndef NODE_DHT_H
#define NODE_DHT_H

#include "esp_err.h"

#include "dht.h"

/* Create alias for dht type */
typedef dht_sensor_type_t node_dht_t;

/**
 * @brief Initialize the DHT sensor interface.
 * 
 * @param gpio_num GPIO number connected to the sensor data pin.
 * @param sensor_type String identifier: "DHT11", "DHT22", or "SI7021".
 * @return esp_err_t ESP_OK on success, ESP_FAIL on invalid parameters.
 */
esp_err_t node_dht_init(node_dht_t sensor_type);

/**
 * @brief Read humidity and temperature from the DHT sensor.
 * 
 * @param humidity Pointer to store humidity in percentage (float).
 * @param temperature Pointer to store temperature in Celsius (float).
 * @return esp_err_t ESP_OK on success, ESP_FAIL on read error.
 */
esp_err_t node_dht_read(float *humidity, float *temperature);

#endif // NODE_DHT_H