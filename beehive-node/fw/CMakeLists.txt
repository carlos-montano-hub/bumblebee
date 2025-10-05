cmake_minimum_required(VERSION 3.16)

# Choose which app to build
set(APP_NAME "esp_http_client_example" CACHE STRING "Select which application to build")
message(STATUS "Building application: ${APP_NAME}")

# Paths
set(APP_PATH ${CMAKE_SOURCE_DIR}/apps/${APP_NAME})
set(CUSTOM_ROOT_PATH ${CMAKE_SOURCE_DIR})
message(STATUS "Root path: ${CUSTOM_ROOT_PATH}")

# Validate app folder
if(NOT EXISTS ${APP_PATH}/CMakeLists.txt)
    message(FATAL_ERROR "Invalid APP_NAME: ${APP_NAME}. Folder not found at ${APP_PATH}")
endif()

# Use separate variables per app
set(APP_COMPONENTS_dht_sensor "${CUSTOM_ROOT_PATH}/middleware/node_dht")
set(APP_COMPONENTS_i2s_recorder "")
set(APP_COMPONENTS_sd_card_example "")
set(APP_COMPONENTS_esp_http_client_example "${CUSTOM_ROOT_PATH}/middleware/dpp_easy_wifi_qr")

# Function to get components for app
function(get_app_components app_name out_var)
    # Construct the variable name dynamically
    set(var_name APP_COMPONENTS_${app_name})
    if(DEFINED ${var_name})
        set(components ${${var_name}})
        set(${out_var} ${components} PARENT_SCOPE)
        message(STATUS "Found special requirements for app ${app_name}: ${components}")
    else()
        message(WARNING "No special requirements defined for app ${app_name}")
        set(${out_var} "" PARENT_SCOPE)
    endif()
endfunction()


# Get middleware/components for this app
get_app_components(${APP_NAME} APP_REQUIREMENTS)

message(STATUS "App requirements: ${APP_REQUIREMENTS}")

# Include the app itself in EXTRA_COMPONENT_DIRS
set(EXTRA_COMPONENT_DIRS ${APP_PATH} ${APP_REQUIREMENTS})

message(STATUS "App - Extra component dirs: ${EXTRA_COMPONENT_DIRS}")

# Include ESP-IDF project
include($ENV{IDF_PATH}/tools/cmake/project.cmake)
project(fw)
