# Min versin cmake check
cmake_minimum_required(VERSION 3.0)

# Project name
project(MyMain)

# This is pointing to submodule dir, and adding this activates
# CMakeLists.txt in that dir. 
add_subdirectory(external/glfw)

# Add executable target ${PROJECT_NAME} (MyMain) will be the name of the executable.
add_executable(${PROJECT_NAME} hi_world.cpp)
