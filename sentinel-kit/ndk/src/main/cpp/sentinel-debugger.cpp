#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <fcntl.h>
#include <jni.h>
#include <pthread.h>
#include <sys/ptrace.h>
#include <sys/system_properties.h>
#include <unistd.h>

static JavaVM *g_debugger_vm = nullptr;
static jobject g_debugger_obj = nullptr;
static jmethodID g_debugger_callback = nullptr;
static bool g_violation_reported = false;

static bool internal_check_tracer_pid() {
  int status_fd = open("/proc/self/status", O_RDONLY);
  if (status_fd == -1)
    return false;

  char buffer[1024];
  ssize_t num_read = read(status_fd, buffer, sizeof(buffer) - 1);
  close(status_fd);

  if (num_read <= 0)
    return false;
  buffer[num_read] = '\0';

  const char *tracer_label = "TracerPid:";
  char *tracer_ptr = strstr(buffer, tracer_label);
  if (tracer_ptr) {
    int pid = atoi(tracer_ptr + strlen(tracer_label));
    if (pid != 0)
      return true;
  }
  return false;
}

static bool internal_check_is_debuggable(JNIEnv *env, jobject context) {
  jclass contextClass = env->GetObjectClass(context);
  jmethodID getAppInfoMethod =
      env->GetMethodID(contextClass, "getApplicationInfo",
                       "()Landroid/content/pm/ApplicationInfo;");
  jobject appInfoObj = env->CallObjectMethod(context, getAppInfoMethod);

  jclass appInfoClass = env->GetObjectClass(appInfoObj);
  jfieldID flagsField = env->GetFieldID(appInfoClass, "flags", "I");
  jint flags = env->GetIntField(appInfoObj, flagsField);

  const int FLAG_DEBUGGABLE = 2;
  return (flags & FLAG_DEBUGGABLE) != 0;
}

static bool internal_check_test_keys() {
  char tags[PROP_VALUE_MAX];
  if (__system_property_get("ro.build.tags", tags) > 0) {
    if (strstr(tags, "test-keys") != nullptr) {
      return true;
    }
  }
  return false;
}

void report_debugger_violation() {
  if (g_debugger_vm && g_debugger_obj && g_debugger_callback) {
    JNIEnv *env;
    jint res = g_debugger_vm->GetEnv((void **)&env, JNI_VERSION_1_6);
    bool attached = false;

    if (res == JNI_EDETACHED) {
      if (g_debugger_vm->AttachCurrentThread(&env, nullptr) == JNI_OK)
        attached = true;
    }

    if (env) {
      env->CallVoidMethod(g_debugger_obj, g_debugger_callback);
    }

    if (attached)
      g_debugger_vm->DetachCurrentThread();
  }
}

void *integrity_monitor(void *arg) {
  while (true) {
    bool current_state = internal_check_tracer_pid();

    if (current_state && !g_violation_reported) {
      report_debugger_violation();
      g_violation_reported = true;
    }

    else if (!current_state && g_violation_reported) {
      g_violation_reported = false;
    }

    sleep(3);
  }
  return nullptr;
}

extern "C" {

JNIEXPORT void JNICALL Java_sentinel_kit_runtime_DebugRuntime_init(
    JNIEnv *env, jobject thiz, jobject instance) {
  if (g_debugger_obj == nullptr) {
    g_debugger_obj = env->NewGlobalRef(instance);
    jclass clazz = env->GetObjectClass(g_debugger_obj);
    g_debugger_callback = env->GetMethodID(clazz, "onDebuggerDetected", "()V");

    pthread_t thread_id;
    pthread_create(&thread_id, nullptr, integrity_monitor, nullptr);
  }
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_DebugDetector_isDebuggerAttached(JNIEnv *env,
                                                        jobject thiz) {
  return (internal_check_tracer_pid()) ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_DebugDetector_isPackageDebuggable(JNIEnv *env,
                                                             jobject thiz,
                                                             jobject context) {
  return internal_check_is_debuggable(env, context) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_DebugDetector_checkTestKeys(JNIEnv *env,
                                                       jobject thiz) {
  return internal_check_test_keys() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  g_debugger_vm = vm;
  return JNI_VERSION_1_6;
}
}