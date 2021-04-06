//#include <jni.h>
//#include <string>
//#include "android/log.h"
//#include <cstdio>
//
//#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , "ffmpeg-cmd", __VA_ARGS__)
//extern "C" {
//
//JNIEXPORT jint JNICALL
//ffmpeg_exec(int argc, char **argv);
//
//JNIEXPORT jint JNICALL
//Java_com_example_ffmpeg07_VideoMerge_mergeVideo(JNIEnv *env, jobject thiz, jobject m_callback,
//                                                jobjectArray commands) {
//    //处理合并视频
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
////    for (int i = 0; i < argc; ++i) {
////        free(argv[i]);
////    }
////    free(argv);
//}
//}



