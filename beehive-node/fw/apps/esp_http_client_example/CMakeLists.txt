# Embed the server root certificate into the final binary
#
# (If this was a component, we would set COMPONENT_EMBED_TXTFILES here.)
set(requires "")
idf_build_get_property(target IDF_TARGET)

list(APPEND requires esp_event esp-tls esp_http_client\ 
    protocol_examples_common nvs_flash esp_netif dpp_easy_wifi_qr)

idf_component_register(SRCS "esp_http_client_example.c"
                    INCLUDE_DIRS "."
                    REQUIRES ${requires}
                    EMBED_TXTFILES howsmyssl_com_root_cert.pem
                    postman_root_cert.pem)
