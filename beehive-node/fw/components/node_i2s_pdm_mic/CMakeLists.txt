idf_component_register(SRCS "node_i2s_pdm_mic.c"
                    INCLUDE_DIRS "include"
                    REQUIRES esp_driver_i2s fatfs i2s_examples_common)
