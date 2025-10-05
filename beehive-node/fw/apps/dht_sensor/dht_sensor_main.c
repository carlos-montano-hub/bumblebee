#include "node_dht.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"


#include <string.h>

void app_main(void)
{
    node_dht_init(DHT_TYPE_DHT11);

    float hum, temp;
    while (1) {
        if (node_dht_read(&hum, &temp) == ESP_OK) {
            printf("Humidity: %.1f%%, Temp: %.1f°C\n", hum, temp);
        }
        vTaskDelay(pdMS_TO_TICKS(2000));
    }
}
