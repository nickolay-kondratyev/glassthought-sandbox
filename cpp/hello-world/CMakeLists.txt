# Min versin cmake check
cmake_minimum_required(VERSION 3.0)

# Project name
project(HelloWorldProjectName)

# Add executable target ${PROJECT_NAME}.executable will be the
# name of the executable.
add_executable(${PROJECT_NAME}.executable hello_world_main.cpp)
