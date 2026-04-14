#include <cstdio>
#include <cstdlib>
#include <fcntl.h>
#include <jni.h>
#include <pthread.h>
#include <string>
#include <sys/stat.h>
#include <unistd.h>
#include <vector>

static JavaVM *g_vm = nullptr;
static jobject g_detector_obj = nullptr;
static jmethodID g_callback_method = nullptr;
static bool g_violation_reported = false;
static pthread_mutex_t g_mutex = PTHREAD_MUTEX_INITIALIZER;

const std::vector<std::string> ROOT_BINARIES = {"/system/bin/su",
                                                "/system/xbin/su",
                                                "/sbin/su",
                                                "/data/local/xbin/su",
                                                "/data/local/bin/su",
                                                "/system/sd/xbin/su",
                                                "/system/bin/failsafe/su",
                                                "/data/adb/magisk",
                                                "/data/adb/ksu",
                                                "/data/adb/ap",
                                                "/data/adb/magisk.db"};

const char *ROOT_PACKAGES[] = {"com.noshufou.android.su",
                               "eu.chainfire.supersu",
                               "com.koushikdutta.superuser",
                               "com.thirdparty.superuser",
                               "com.yellowes.su",
                               "com.topjohnwu.magisk",
                               "com.kingroot.kinguser",
                               "com.zhiqupk.root.global"};

const std::vector<std::string> SUSPICIOUS_MOUNTS = {
    "magisk",
    "core/img",
    "mirror",
    "history",
    "init.magisk"};

void set_violation_status(bool status) {
  pthread_mutex_lock(&g_mutex);
  g_violation_reported = status;
  pthread_mutex_unlock(&g_mutex);
}

bool internal_check_binaries() {
  struct stat buffer{};
  for (const auto &path : ROOT_BINARIES) {
    if (stat(path.c_str(), &buffer) == 0)
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
  for (const auto &mnt : SUSPICIOUS_MOUNTS) {
    if (mounts.find(mnt) != std::string::npos)
      return true;
  }

  return false;
}

bool internal_check_su_command() {
  FILE *pipe = popen("which su", "r");
  if (!pipe)
    return false;
  char line[128];
  bool found = (fgets(line, sizeof(line), pipe) != nullptr);
  pclose(pipe);
  return found;
}

bool internal_check_apps(JNIEnv *env, jobject context) {
  jclass contextClass = env->GetObjectClass(context);
  jmethodID getPackageManager = env->GetMethodID(contextClass, "getPackageManager","()Landroid/content/pm/PackageManager;");
  jobject packageManager = env->CallObjectMethod(context, getPackageManager);

  jclass pmClass = env->GetObjectClass(packageManager);
  jmethodID getPackageInfo = env->GetMethodID(pmClass, "getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

  for (auto &pkgName : ROOT_PACKAGES) {
    jstring jPkg = env->NewStringUTF(pkgName);

    jobject pkgInfo = env->CallObjectMethod(packageManager, getPackageInfo, jPkg, 0);
    if (env->ExceptionCheck()) {
      env->ExceptionClear();
      pkgInfo = nullptr;
    }

    env->DeleteLocalRef(jPkg);

    if (pkgInfo != nullptr)
      return JNI_TRUE;
  }

  return JNI_FALSE;
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

    if (internal_check_binaries() || internal_check_mounts() ||
        internal_check_su_command()) {
      current_violation = true;
    }

    if (current_violation && !g_violation_reported) {
      report_root_violation();
    }

    set_violation_status(current_violation);
    sleep(3);
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
  return JNI_VERSION_1_6;
}
}