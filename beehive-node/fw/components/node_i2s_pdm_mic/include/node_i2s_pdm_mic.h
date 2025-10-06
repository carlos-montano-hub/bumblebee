#ifndef NODE_I2S_PDM_MIC_H
#define NODE_I2S_PDM_MIC_H

#include <stdint.h>
#include "esp_err.h"

void node_i2s_pdm_mic_init(void);
void node_i2s_pdm_mic_deinit(void);

esp_err_t node_i2s_pdm_mic_read(void *dest, size_t size, size_t *bytes_read, uint32_t timeout_ms);

#endif // NODE_I2S_PDM_MIC_H