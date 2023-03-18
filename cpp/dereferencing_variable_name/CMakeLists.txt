# Min versin cmake check
cmake_minimum_required(VERSION 3.0)

function(print_var_and_check_file_exists var_name)
  if(NOT EXISTS ${${var_name}})
    # Use fatal error to stop CMake from continuing
    # message(FATAL_ERROR "File ${${var_name}} does NOT exist (defined by ${var_name})")
    message("File ${${var_name}} does NOT exist (defined by ${var_name})")
  endif()

  if(EXISTS ${${var_name}})
    message("File ${${var_name}} exists (defined by ${var_name})")
  endif()
endfunction()

set(non_existent_file "/path/to/my/file.txt")
set(existent_file "/tmp")

print_var_and_check_file_exists("existent_file")
print_var_and_check_file_exists("non_existent_file")


# Project name
project(HelloWorldProjectName)

# Add executable target ${PROJECT_NAME}.executable will be the
# name of the executable.
add_executable(${PROJECT_NAME}.executable hello_world_main.cpp)
