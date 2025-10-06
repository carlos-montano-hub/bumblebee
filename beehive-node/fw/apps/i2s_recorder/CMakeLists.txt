idf_component_register(SRCS "i2s_recorder_main.c"
                    PRIV_REQUIRES esp_driver_i2s fatfs i2s_examples_common node_i2s_pdm_mic node_sd
                    INCLUDE_DIRS)
