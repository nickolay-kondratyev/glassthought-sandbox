cmake_minimum_required(VERSION 3.13)

# Project name
project(LibConsumer)

# Add executable target ${PROJECT_NAME}.executable will be the
# name of the executable.
add_executable(LibConsumer main.cpp)

target_link_directories(LibConsumer PRIVATE ${CMAKE_SOURCE_DIR}/../LibProducer/build/)

# The libary that is built will be named libmymath.a, this will be sorted
# out for us and we dont need to provide the 'lib' prefix or the '.a' suffix.
target_link_libraries(LibConsumer mymath)
