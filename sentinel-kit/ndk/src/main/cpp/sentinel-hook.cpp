#include <android/log.h>
#include <fstream>
#include <jni.h>
#include <string>
#include <unistd.h>

#include <android/log.h>
#include <fstream>
#include <jni.h>
#include <string>
#include <unistd.h>

#define LOG_TAG "Sentinel"
#if defined(DEBUG) || defined(_DEBUG)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#define LOGI(...) ((void)0)
#define LOGE(...) ((void)0)
#endif

extern "C" {

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_HookDetector_isFridaDetected(JNIEnv *env, jobject thiz) {
  char line[512];
  FILE *fp = fopen("/proc/self/maps", "r");
  if (fp == nullptr)
    return JNI_FALSE;

  bool found = false;
  while (fgets(line, sizeof(line), fp)) {
    if (strstr(line, "frida") || strstr(line, "gum-js") ||
        strstr(line, "linjector")) {
      found = true;
      break;
    }
  }

  fclose(fp);

  return found ? JNI_TRUE : JNI_FALSE;
}
}