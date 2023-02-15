# Min versin cmake check
cmake_minimum_required(VERSION 3.0)

# Project name
project(MyMain)
add_executable(${PROJECT_NAME} hi_world.cpp)

# This is pointing to submodule dir, and adding this activates
# CMakeLists.txt in that dir.
add_subdirectory(external/glfw)

target_include_directories(${PROJECT_NAME}
  PUBLIC external/glfw/include
)

# https://cmake.org/cmake/help/latest/command/target_link_directories.html
target_link_directories(${PROJECT_NAME}
  PRIVATE external/glfw/src
)

target_link_libraries(${PROJECT_NAME}
  glfw
)
