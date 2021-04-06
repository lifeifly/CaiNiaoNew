//
// Created by Administrator on 2021/3/31.
//

#include <jni.h>
#include <string.h>
#include "ffmpegCmd.h"
#include "android/log.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , "ffmpeg-cmd", __VA_ARGS__)
extern "C" {
#include "ffmpeg.h"
void callback(int current, int total);
JNIEXPORT jint JNICALL
Java_com_example_ffmpeg07_VideoMerge_mergeVideo(JNIEnv *env, jobject thiz,
                                                jobjectArray commands) {
//    int argc = env->GetArrayLength(commands);
////    char **argv = static_cast<char **>(malloc(sizeof(char *) * argc));
//    char *argv[argc];
//    for (int i = 0; i < argc; i++) {
//        jstring j_param = static_cast<jstring>(env->GetObjectArrayElement(commands, i));
//        argv[i] = const_cast<char *>(env->GetStringUTFChars(j_param, NULL));
//        LOGD("argcmd=%s", argv[i]);
//    }
//
//    return ffmpeg_exec(argc, argv);
    int argc = (*env).GetArrayLength(commands);
    char **argv = (char **) malloc(argc * sizeof(char *));
    int i;
    int result;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env).GetObjectArrayElement(commands, i);
        char *temp = (char *) (*env).GetStringUTFChars(jstr, 0);
        argv[i] = static_cast<char *>(malloc(1024));
        strcpy(argv[i], temp);
        (*env).ReleaseStringUTFChars(jstr, temp);
    }
    //执行ffmpeg命令
    result = ffmpeg_exec(argc, argv, callback);
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return result;

}

}

void callback(int current, int total) {
    LOGD("合并进度：%d/%d", current, total);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_ffmpeg07_VideoMerge_intraVideo(JNIEnv *env, jobject thiz, jobjectArray commands) {
    int argc = (*env).GetArrayLength(commands);
    char **argv = (char **) malloc(argc * sizeof(char *));
    int i;
    int result;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env).GetObjectArrayElement(commands, i);
        char *temp = (char *) (*env).GetStringUTFChars(jstr, 0);
        argv[i] = static_cast<char *>(malloc(1024));
        strcpy(argv[i], temp);
        (*env).ReleaseStringUTFChars(jstr, temp);
    }
    //执行ffmpeg命令
    result = ffmpeg_exec(argc, argv, callback);
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return result;
}