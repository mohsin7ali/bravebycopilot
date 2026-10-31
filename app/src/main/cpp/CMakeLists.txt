cmake_minimum_required(VERSION 3.22.1)

project(native_blocker LANGUAGES CXX)

add_library(
    native_blocker
    SHARED
    native_blocker.cpp
)

target_compile_features(native_blocker PRIVATE cxx_std_17)

find_library(log-lib log)

target_link_libraries(native_blocker
    ${log-lib}
)
