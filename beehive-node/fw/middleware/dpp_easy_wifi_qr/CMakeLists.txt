idf_component_register(SRCS "dpp_easy_wifi_qr.c"
                    INCLUDE_DIRS "include"
                    REQUIRES esp_wifi esp_event esp_netif nvs_flash
                    wpa_supplicant)
