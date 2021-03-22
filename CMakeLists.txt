cmake_minimum_required(VERSION 3.14)

project(_clingo)

find_package(SWIG REQUIRED)
find_package(Java REQUIRED)
find_package(JNI REQUIRED)
find_package(Clingo REQUIRED)
include(UseJava)
include(UseSWIG)
include_directories(${JNI_INCLUDE_DIRS})

# Build the C++ code into a dynamic library: example.dll (on Windows) or libexample.so (on Linux)
set_property(SOURCE _clingo.i PROPERTY USE_TARGET_INCLUDE_DIRECTORIES ON)
set_property(SOURCE _clingo.i PROPERTY CPLUSPLUS ON)
set_property(SOURCE _clingo.i PROPERTY COMPILE_OPTIONS "-package;org.jclingo.swig")

swig_add_library(
    _clingo 
    TYPE MODULE
    LANGUAGE java 
    SOURCES _clingo.i 
    OUTPUT_DIR "${CMAKE_CURRENT_BINARY_DIR}/generated/org/jclingo/swig"
)
swig_link_libraries(_clingo libclingo)

# TODO: this is just for development and requires to configure/build twice
file(GLOB _clingo_generated "${CMAKE_CURRENT_BINARY_DIR}/generated/org/jclingo/swig/*.java")

# For convenience we copy the dynamic library to the current build folder
add_custom_command(
    TARGET _clingo 
    POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different $<TARGET_FILE:_clingo> ${CMAKE_CURRENT_BINARY_DIR}
)

# Build the Java code into ExampleJNI.jar
set(CMAKE_JAR_CLASSES_PREFIX org/jclingo/swig)
add_jar(jclingo_swig SOURCES ${_clingo_generated})
add_dependencies(jclingo_swig _clingo)

set(CMAKE_JAR_CLASSES_PREFIX)
add_jar(example SOURCES example.java ENTRY_POINT example INCLUDE_JARS jclingo_swig)
