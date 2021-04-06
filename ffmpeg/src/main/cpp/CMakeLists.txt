# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("ffmpeg07")
aux_source_directory(. SRC_LIST) # 搜索当前目录下的所有.cpp文件

include_directories(include/)

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED


        include/cmdutils.c
        include/ffmpeg.c
        include/ffmpeg_filter.c
        include/ffmpeg_opt.c
        include/ffmpeg_hw.c
        include/libavutil/eval.c
        include/libavutil/reverse.c
        ffmpegCmd.cpp
        )


find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

set(FF ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)
add_library(avfilter SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avcodec SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)

set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libavfilter.so)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libavformat.so)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libavcodec.so)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libavutil.so)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libswresample.so)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${FF}/${ANDROID_ABI}/libswscale.so)



target_link_libraries( # Specifies the target library.
        native-lib
        avfilter
        avformat
        avcodec
        avutil
        swresample
        swscale


        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})