# Min versin cmake check
cmake_minimum_required(VERSION 3.0)

# Project name
project(MyMain)

option(MY_OPTION "My Option" ON)


if (MY_OPTION)
    message("--------------------------------------------------------------------------------")
    message("My Option is ON")
    message("--------------------------------------------------------------------------------")
endif()


add_executable(${PROJECT_NAME} hi_world.cpp)

