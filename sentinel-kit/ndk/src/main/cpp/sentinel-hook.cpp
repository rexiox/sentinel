#include <fstream>
#include <jni.h>
#include <pthread.h>
#include <string>
#include <unistd.h>
#include <vector>

static JavaVM *g_vm = nullptr;
static jobject g_detector_obj = nullptr;
static jmethodID g_callback_method = nullptr;
static bool g_last_memory_violation = false;
static bool g_last_stack_violation = false;

const std::vector<std::string> HOOK_PACKAGES = {
    "de.robv.android.xposed",
    "com.topjohnwu.lsposed",
    "org.meowcat.edxposed.manager",
    "com.saurik.substrate",
    "com.devadvance.rootcloak",
    "com.devadvance.rootcloakplus"};

void sentinel_report_violation() {
  if (g_vm && g_detector_obj && g_callback_method) {
    JNIEnv *env;
    jint res = g_vm->GetEnv((void **)&env, JNI_VERSION_1_6);
    bool attached = false;
    if (res == JNI_EDETACHED) {
      if (g_vm->AttachCurrentThread(&env, nullptr) == JNI_OK) {
        attached = true;
      }
    }

    if (env) {
      env->CallVoidMethod(g_detector_obj, g_callback_method);
    }

    if (attached)
      g_vm->DetachCurrentThread();
  }
}

bool internal_check_memory_maps() {
  char line[512];
  FILE *fp = fopen("/proc/self/maps", "r");
  if (fp == nullptr)
    return false;

  bool found = false;
  while (fgets(line, sizeof(line), fp)) {
    if (strstr(line, "frida") || strstr(line, "gum-js") ||
        strstr(line, "XposedBridge.jar")) {
      found = true;
      break;
    }
  }
  fclose(fp);
  return found;
}

bool internal_check_stack_trace(JNIEnv *env) {
  jclass exClass = env->FindClass("java/lang/Exception");
  jmethodID exInit = env->GetMethodID(exClass, "<init>", "()V");
  jobject exObj = env->NewObject(exClass, exInit);
  jmethodID getSTMethod = env->GetMethodID(exClass, "getStackTrace","()[Ljava/lang/StackTraceElement;");
  auto stackTrace = (jobjectArray)env->CallObjectMethod(exObj, getSTMethod);

  if (stackTrace == nullptr)
    return false;

  int len = env->GetArrayLength(stackTrace);
  jclass stElementClass = env->FindClass("java/lang/StackTraceElement");
  jmethodID getClassNameMethod =env->GetMethodID(stElementClass, "getClassName", "()Ljava/lang/String;");

  for (int i = 0; i < len; i++) {
    jobject element = env->GetObjectArrayElement(stackTrace, i);
    auto classNameObj =
        (jstring)env->CallObjectMethod(element, getClassNameMethod);
    const char *classNameCStr = env->GetStringUTFChars(classNameObj, nullptr);
    std::string className(classNameCStr);

    for (const auto &hookPkg : HOOK_PACKAGES) {
      if (className.find(hookPkg) != std::string::npos) {
        env->ReleaseStringUTFChars(classNameObj, classNameCStr);
        env->DeleteLocalRef(element);
        return true;
      }
    }
    env->ReleaseStringUTFChars(classNameObj, classNameCStr);
    env->DeleteLocalRef(element);
  }

  return false;
}

void *integrity_monitor(void *arg) {
  while (true) {
    bool current_memory_state = internal_check_memory_maps();
    if (current_memory_state && !g_last_memory_violation) {
      sentinel_report_violation();
    }
    g_last_memory_violation = current_memory_state;

    JNIEnv *env;
    if (g_vm->AttachCurrentThread(&env, nullptr) == JNI_OK) {
      bool current_stack_state = internal_check_stack_trace(env);
      if (current_stack_state && !g_last_stack_violation) {
        sentinel_report_violation();
      }
      g_last_stack_violation = current_stack_state;
      g_vm->DetachCurrentThread();
    }

    sleep(3);
  }

  return nullptr;
}

extern "C" {

JNIEXPORT void JNICALL Java_sentinel_kit_runtime_HookRuntime_init(
    JNIEnv *env, jobject thiz, jobject instance) {
  g_detector_obj = env->NewGlobalRef(instance);
  jclass clazz = env->GetObjectClass(g_detector_obj);
  g_callback_method = env->GetMethodID(clazz, "onHookDetected", "()V");
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_HookDetector_isFridaDetected(JNIEnv *env, jobject thiz) {
  return internal_check_memory_maps() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jstring JNICALL
Java_sentinel_kit_detector_HookDetector_checkStackTraceManually(JNIEnv *env, jobject thiz) {
  if (internal_check_stack_trace(env))
    return env->NewStringUTF("Violation Detected");
  return nullptr;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  g_vm = vm;
  pthread_t thread_id;
  pthread_create(&thread_id, nullptr, integrity_monitor, nullptr);
  return JNI_VERSION_1_6;
}
}