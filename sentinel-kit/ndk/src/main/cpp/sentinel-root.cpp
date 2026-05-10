#include "sentinel-obfuscate.hpp"
#include <cstdio>
#include <cstdlib>
#include <fcntl.h>
#include <jni.h>
#include <pthread.h>
#include <string>
#include <sys/stat.h>
#include <unistd.h>
#include <vector>

static jclass g_pm_class = nullptr;
static jclass g_context_class = nullptr;
static jmethodID g_getPackageManagerID = nullptr;
static jmethodID g_getPackageInfoID = nullptr;

static JavaVM *g_vm = nullptr;
static jobject g_detector_obj = nullptr;
static jmethodID g_callback_method = nullptr;
static bool g_violation_reported = false;
static pthread_mutex_t g_mutex = PTHREAD_MUTEX_INITIALIZER;

void set_violation_status(bool status) {
  pthread_mutex_lock(&g_mutex);
  g_violation_reported = status;
  pthread_mutex_unlock(&g_mutex);
}

bool internal_check_binaries() {
  struct stat buffer{};
  for (const auto &path : ROOT_BINARIES) {
    if (stat(transform(path).c_str(), &buffer) == 0)
      return true;
  }

  return false;
}

bool internal_check_mounts() {
  char buf[4096];
  int fd = open("/proc/mounts", O_RDONLY);
  if (fd < 0)
    return false;
  ssize_t n = read(fd, buf, sizeof(buf) - 1);
  close(fd);
  if (n <= 0)
    return false;
  buf[n] = '\0';
  std::string mounts(buf);
  for (const auto &mnt : ROOT_SUSPICIOUS_MOUNTS) {
    if (mnt == nullptr)
      return false;

    if (mounts.find(transform(mnt)) != std::string::npos)
      return true;
  }

  return false;
}

bool internal_check_su_command() {
  for (const char *path : ROOT_SU_PATHS) {
    if (access(transform(path).c_str(), F_OK) == 0)
      return true;
  }
  return false;
}

bool internal_check_apps(JNIEnv *env, jobject context) {
  if (context == nullptr || g_getPackageManagerID == nullptr || g_getPackageInfoID == nullptr) {
    return JNI_FALSE;
  }

  jobject packageManager =env->CallObjectMethod(context, g_getPackageManagerID);

  if (env->ExceptionCheck() || !packageManager) {
    env->ExceptionClear();
    return JNI_FALSE;
  }

  bool found = false;

  for (const char *pkgName : ROOT_PACKAGES) {
    jstring jPkg = env->NewStringUTF(transform(pkgName).c_str());
    jobject pkgInfo =env->CallObjectMethod(packageManager, g_getPackageInfoID, jPkg, 0);

    if (env->ExceptionCheck()) {
      env->ExceptionClear();
      pkgInfo = nullptr;
    }

    env->DeleteLocalRef(jPkg);

    if (pkgInfo != nullptr) {
      env->DeleteLocalRef(pkgInfo);
      found = true;
      break;
    }
  }

  env->DeleteLocalRef(packageManager);

  return found ? JNI_TRUE : JNI_FALSE;
}

void report_root_violation() {
  if (g_vm && g_detector_obj && g_callback_method) {
    JNIEnv *env;
    jint res = g_vm->GetEnv((void **)&env, JNI_VERSION_1_6);
    bool attached = false;
    if (res == JNI_EDETACHED) {
      if (g_vm->AttachCurrentThread(&env, nullptr) == JNI_OK)
        attached = true;
    }

    if (env) {
      env->CallVoidMethod(g_detector_obj, g_callback_method);
    }

    if (attached)
      g_vm->DetachCurrentThread();
  }
}

void *integrity_monitor(void *arg) {
  while (true) {
    bool current_violation = false;
    const char *reason = nullptr;

    if (internal_check_binaries() || internal_check_mounts() || internal_check_su_command()) {
      current_violation = true;
    }

    if (current_violation && !g_violation_reported) {
      report_root_violation();
    }

    set_violation_status(current_violation);
    sleep(30);
  }

  return nullptr;
}

extern "C" {

JNIEXPORT void JNICALL Java_sentinel_kit_runtime_RootRuntime_init(
    JNIEnv *env, jobject thiz, jobject instance) {
  if (g_detector_obj == nullptr) {
    g_detector_obj = env->NewGlobalRef(instance);
    jclass clazz = env->GetObjectClass(g_detector_obj);
    g_callback_method = env->GetMethodID(clazz, "onRootDetected", "()V");

    pthread_t thread_id;
    pthread_create(&thread_id, nullptr, integrity_monitor, nullptr);
  }
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkBinaries(JNIEnv *env, jobject) {
  return internal_check_binaries() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkMounts(JNIEnv *env, jobject) {
  return internal_check_mounts() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkSuCommand(JNIEnv *env, jobject) {
  return internal_check_su_command() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_sentinel_kit_detector_RootDetector_checkApps(JNIEnv *env, jobject thiz, jobject context) {
  return internal_check_apps(env, context) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  g_vm = vm;
  JNIEnv *env;
  if (vm->GetEnv((void **)&env, JNI_VERSION_1_6) != JNI_OK) return JNI_ERR;

  jclass localContextClass = env->FindClass("android/content/Context");
  g_context_class = (jclass)env->NewGlobalRef(localContextClass);
  g_getPackageManagerID = env->GetMethodID(g_context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");

  jclass localPmClass = env->FindClass("android/content/pm/PackageManager");
  g_pm_class = (jclass)env->NewGlobalRef(localPmClass);
  g_getPackageInfoID = env->GetMethodID(g_pm_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

  env->DeleteLocalRef(localContextClass);
  env->DeleteLocalRef(localPmClass);

  return JNI_VERSION_1_6;
}
}