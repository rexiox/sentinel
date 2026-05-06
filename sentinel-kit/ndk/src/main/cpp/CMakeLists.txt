cmake_minimum_required(VERSION 3.22.1)

project("sentinel-detectors")

set(CMAKE_ANDROID_PAGE_SIZE 16384)
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")

if(CMAKE_BUILD_TYPE STREQUAL "Debug")
    add_definitions(-DDEBUG)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")
endif()

find_library(log-lib log)

add_library(sentinel-root SHARED sentinel-root.cpp)
target_link_libraries(sentinel-root ${log-lib})

add_library(sentinel-tamper SHARED sentinel-tamper.cpp)
target_link_libraries(sentinel-tamper ${log-lib})

add_library(sentinel-hook SHARED sentinel-hook.cpp)
target_link_libraries(sentinel-hook ${log-lib})

add_library(sentinel-emulator SHARED sentinel-emulator.cpp)
target_link_libraries(sentinel-emulator ${log-lib})

add_library(sentinel-debugger SHARED sentinel-debugger.cpp)
target_link_libraries(sentinel-debugger ${log-lib})