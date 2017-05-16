#Require correct version of cmake
cmake_minimum_required (VERSION 2.8)

#Set project name. Call this whatever you want
project (ResetVision)

#Find libraries
find_package(OpenCV REQUIRED)
find_package(ZeroMQ REQUIRED)

#Set flags for C++ standard libraries
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

#Include directories
include_directories(${OpenCV_INCLUDE_DIRS})
include_directories(${allwpilib/wpilibc/athena/include})
include_directories(${WPILib.h})
include_directories(/usr/local/include)
include_directories(/usr/local/lib)

#Disabling unneeded CUDA stuff
unset(CUDA_USE_STATIC_CUDA_RUNTIME CACHE)
option(CUDA_USE_STATIC_CUDA_RUNTIME OFF)

#Creating executable for vision script. IMPORTANT! Change the resetFramework.cpp to the name of your script.
#The first part is the name of the executable and the second part is the script name.
#You can add another script by copying the line and changing first and second part for your second script
add_executable(resetFramework resetFramework.cpp)


#Link libraries to the executable. Change resetFramework to script name (without the .cpp).
#You can add another script by copying all the lines below and change the resetFramework to the name of your 2nd script.

target_link_libraries(resetFramework ${OpenCV_LIBS})
# The 2 line below is for ZeroMQ. Remove if not using it.
target_link_libraries(resetFramework zmq)
target_link_libraries(resetFramework ${zmq.h})

target_link_libraries(resetFramework ${allwpilib/wpilibc/athena/include})
target_link_libraries(resetFramework ${WPILib.h})


