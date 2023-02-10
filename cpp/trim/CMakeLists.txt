cmake_minimum_required(VERSION 3.0.0)

project(TrimProjectName)

add_executable(trim-example-app main.cpp)

# Just running make will not install the executable.
# You need to run
#
# make install
#
# DESTINATION bin will be written to some system defined directory
# on MAC for me it ended up in
# -- Installing: /usr/local/bin/trim-example-app
install(TARGETS trim-example-app DESTINATION bin)






