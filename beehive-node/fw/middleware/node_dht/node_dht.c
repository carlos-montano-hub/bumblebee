#include "node_dht.h"
#include "dht.h"
#include "esp_log.h"
#include "sdkconfig.h"

#include <string.h>

static const char *TAG = "node_dht";

static gpio_num_t dht_gpio = CONFIG_NODE_DHT_GPIO;
static node_dht_t dht_type = DHT_TYPE_DHT11; // Default to DHT11

esp_err_t node_dht_init(node_dht_t sensor_type)
{
    if (sensor_type != DHT_TYPE_DHT11 && sensor_type != DHT_TYPE_AM2301 && sensor_type != DHT_TYPE_SI7021) {
        ESP_LOGE(TAG, "Invalid sensor type");
        return ESP_FAIL;
    }
    dht_type = sensor_type;
    ESP_LOGI(TAG, "DHT sensor initialized on GPIO %d, type %d", dht_gpio, dht_type);
    return ESP_OK;
}

esp_err_t node_dht_read(float *humidity, float *temperature)
{
    if (dht_gpio < 0) {
        ESP_LOGE(TAG, "DHT not initialized");
        return ESP_FAIL;
    }

    esp_err_t res = dht_read_float_data(dht_type, dht_gpio, humidity, temperature);
    if (res != ESP_OK) {
        ESP_LOGW(TAG, "Failed to read DHT data");
    } else {
        ESP_LOGI(TAG, "Humidity: %.1f%%, Temperature: %.1f°C", *humidity, *temperature);
    }

    return res;
}
