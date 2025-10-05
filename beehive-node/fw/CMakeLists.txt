# The following lines of boilerplate have to be in your project's CMakeLists
# in this exact order for cmake to work correctly
cmake_minimum_required(VERSION 3.16)

# Choose which app to build
set(APP_NAME "i2s_recorder" CACHE STRING "Select which application to build")

message(STATUS "Building application: ${APP_NAME}")

# Path to app
set(APP_PATH ${CMAKE_SOURCE_DIR}/apps/${APP_NAME})

if (EXISTS ${APP_PATH}/CMakeLists.txt)
    set(EXTRA_COMPONENT_DIRS ${APP_PATH})
else()
    message(FATAL_ERROR "Invalid APP_NAME: ${APP_NAME}. Folder not found at ${APP_PATH}")
endif()

include($ENV{IDF_PATH}/tools/cmake/project.cmake)
project(fw)
