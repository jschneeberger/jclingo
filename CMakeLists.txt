cmake_minimum_required(VERSION 3.14)

project(_clingo)

find_package(SWIG REQUIRED)
find_package(Java REQUIRED)
find_package(JNI REQUIRED)
find_package(Clingo REQUIRED)
include(UseJava)
include(UseSWIG)
include_directories(${JNI_INCLUDE_DIRS})

set(CMAKE_SWIG_OUTDIR ${CMAKE_CURRENT_BINARY_DIR}/swig)

# TODO: this is just for development and requires to configure/build twice
file(GLOB _clingo_generated ${CMAKE_SWIG_OUTDIR}/*.java)

# Build the C++ code into a dynamic library: example.dll (on Windows) or libexample.so (on Linux)
set_property(SOURCE _clingo.i PROPERTY USE_TARGET_INCLUDE_DIRECTORIES ON)
swig_add_library(
    _clingo 
    TYPE MODULE
    LANGUAGE java 
    SOURCES _clingo.i 
)
swig_link_libraries(_clingo libclingo)

# For convenience we copy the dynamic library to the current build folder
add_custom_command(
    TARGET _clingo 
    POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different $<TARGET_FILE:_clingo> ${CMAKE_CURRENT_BINARY_DIR}
)

# Build the Java code into ExampleJNI.jar
add_jar(
    example
    SOURCES
    ${_clingo_generated}
    SOURCES example.java
    ENTRY_POINT example
)
add_dependencies(example _clingo)
