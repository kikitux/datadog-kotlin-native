cmake_minimum_required(VERSION 3.8)
set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_LIST_DIR}/KotlinCMakeModule)
project(toddog Kotlin)
konanc_executable(
        NAME myddogapp
        SOURCES src/myapp.kt src/toddog/toddog.kt src/toddog/socket.kt
)
