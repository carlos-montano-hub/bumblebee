# The following lines of boilerplate have to be in your project's CMakeLists
# in this exact order for cmake to work correctly
cmake_minimum_required(VERSION 3.16)

# Select which app to build
# Options: wifi_example, sd_card_example
option(BUILD_SD_CARD_EXAMPLE "Build SD card example app" OFF)

if(BUILD_SD_CARD_EXAMPLE)
    set(EXTRA_COMPONENT_DIRS apps/sd_card_example)
else()
    set(EXTRA_COMPONENT_DIRS apps/esp_http_client_example)
endif()

include($ENV{IDF_PATH}/tools/cmake/project.cmake)
project(fw)
