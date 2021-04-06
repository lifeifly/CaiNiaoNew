//
// Created by Administrator on 2021/3/31.
//
#include <jni.h>

#ifndef FFMPEG07_FFMPEGCMD_H
#define FFMPEG07_FFMPEGCMD_H


#ifdef __cplusplus
extern "C"{
#endif
JNIEXPORT jint JNICALL
Java_com_example_ffmpeg07_VideoMerge_mergeVideo(JNIEnv *env, jobject thiz,
                                                jobjectArray commands);
#ifdef __cplusplus
}
#endif

#endif //FFMPEG07_FFMPEGCMD_H
