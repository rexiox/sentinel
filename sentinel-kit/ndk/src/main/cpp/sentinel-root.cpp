#include <cstdio>
#include <cstdlib>
#include <fcntl.h>
#include <jni.h>
#include <string>
#include <sys/stat.h>
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

const char *ROOT_PACKAGES[] = {"com.noshufou.android.su",
                               "eu.chainfire.supersu",
                               "com.koushikdutta.superuser",
                               "com.thirdparty.superuser",
                               "com.yellowes.su",
                               "com.topjohnwu.magisk",
                               "com.kingroot.kinguser",
                               "com.zhiqupk.root.global"};

const char *ROOT_BINARIES[] = {"/system/app/Superuser.apk",
                               "/sbin/su",
                               "/system/bin/su",
                               "/system/xbin/su",
                               "/data/local/xbin/su",
                               "/data/local/bin/su",
                               "/system/sd/xbin/su",
                               "/system/bin/failsafe/su",
                               "/data/local/su",
                               "/su/bin/su",
                               "/data/adb/magisk",
                               "/data/adb/ksu",
                               "/data/adb/ap",
                               "/data/adb/magisk.db"};

const char *SUSPICIOUS_MOUNTS[] = {"magisk", "core/img", "mirror", "history",
                                   "init.magisk"};

bool fileExists(const char *path) {
  struct stat buffer{};
  return (stat(path, &buffer) == 0);
}

JNIEXPORT jboolean JNICALL Java_sentinel_kit_detector_RootDetector_checkApps(
    JNIEnv *env, jobject, jobject context) {

  jclass contextClass = env->GetObjectClass(context);
  jmethodID getPackageManager =
      env->GetMethodID(contextClass, "getPackageManager",
                       "()Landroid/content/pm/PackageManager;");
  jobject packageManager = env->CallObjectMethod(context, getPackageManager);

  jclass pmClass = env->GetObjectClass(packageManager);
  jmethodID getPackageInfo =
      env->GetMethodID(pmClass, "getPackageInfo",
                       "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

  for (auto &pkgName : ROOT_PACKAGES) {
    jstring jPkg = env->NewStringUTF(pkgName);

    jobject pkgInfo =
        env->CallObjectMethod(packageManager, getPackageInfo, jPkg, 0);
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

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkBinaries(JNIEnv *env, jobject) {
  for (const auto &path : ROOT_BINARIES) {
    if (fileExists(path))
      return JNI_TRUE;
  }
  return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkMounts(JNIEnv *env, jobject) {
  int fd = open("/proc/mounts", O_RDONLY);
  if (fd < 0)
    return JNI_FALSE;

  char buffer[4096];
  ssize_t n = read(fd, buffer, sizeof(buffer) - 1);
  close(fd);
  if (n <= 0)
    return JNI_FALSE;
  buffer[n] = '\0';

  std::string mounts(buffer);
  for (const auto &mount : SUSPICIOUS_MOUNTS) {
    if (mounts.find(mount) != std::string::npos)
      return JNI_TRUE;
  }
  return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_sentinel_kit_detector_RootDetector_checkSuCommand(JNIEnv *env, jobject) {
  FILE *pipe = popen("which su", "r");
  if (!pipe)
    return JNI_FALSE;
  char line[128];
  bool found = false;
  if (fgets(line, sizeof(line), pipe))
    found = true;
  pclose(pipe);
  return found ? JNI_TRUE : JNI_FALSE;
}
}