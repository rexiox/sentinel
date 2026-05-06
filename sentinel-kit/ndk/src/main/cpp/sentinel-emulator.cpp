#include <jni.h>
#include <string>
#include <fstream>
#include <sys/system_properties.h>
#include <unistd.h>
#include <vector>
#include <algorithm>

#ifndef PROP_VALUE_MAX
#define PROP_VALUE_MAX 92
#endif

std::string getProperty(const char* name) {
  char value[PROP_VALUE_MAX];
  int length = __system_property_get(name, value);
  return (length > 0) ? std::string(value) : "";
}

std::string checkPipesAndFiles() {
  const std::vector<std::string> pipes = {
      "/dev/socket/qemud",
      "/dev/qemu_pipe",
      "/mnt/windows/BstSharedFolder",
      "/dev/socket/genyd",
      "/dev/socket/baseband_genyd",
      "/system/bin/qemu-props",
      "/system/lib/libc_malloc_debug_qemu.so",
      "/sys/qemu_trace",
      "/data/data/com.bluestacks.home"
  };

  for (const auto& path : pipes) {
    if (access(path.c_str(), F_OK) == 0) {
      return "[File/Pipe: " + path + "] ";
    }
  }
  return "";
}

std::string checkEmulatorProperties() {
  const std::vector<std::string> propKeys = {
      "ro.hardware",
      "ro.product.name",
      "ro.product.model",
      "ro.product.manufacturer",
      "ro.product.brand",
      "ro.product.device",
      "ro.product.board",
      "ro.board.platform"
  };

  const std::vector<std::string> searchTerms = {
      "google_sdk",
      "emulator",
      "android_sdk",
      "genymotion",
      "goldfish",
      "vbox86",
      "sdk_gphone",
      "bluestacks",
      "ranchu"
  };

  for (const auto& key : propKeys) {
    std::string val = getProperty(key.c_str());
    if (val.empty()) continue;

    std::string lowerVal = val;
    std::transform(lowerVal.begin(), lowerVal.end(), lowerVal.begin(), ::tolower);

    for (const auto& term : searchTerms) {
      if (lowerVal.find(term) != std::string::npos) {
        return "[Prop: " + key + "=" + val + "] ";
      }
    }
  }
  return "";
}

std::string checkCpuInfo() {
  std::ifstream cpuinfo("/proc/cpuinfo");
  if (cpuinfo.is_open()) {
    std::string line;
    while (std::getline(cpuinfo, line)) {
      if (line.find("Intel") != std::string::npos ||
          line.find("AMD") != std::string::npos ||
          line.find("QEMU") != std::string::npos) {
        cpuinfo.close();
        return "[CPU: x86/QEMU detected] ";
      }
    }
    cpuinfo.close();
  }
  return "";
}

std::string checkExtraApps() {
  const char* extraFiles[] = {
      "/data/data/com.bluestacks.appmart",
      "/data/data/com.bluestacks.settings",
      "/system/bin/nemu"
  };
  for (const char* file : extraFiles) {
    if (access(file, F_OK) == 0) {
      return "[Extra: " + std::string(file) + "] ";
    }
  }
  return "";
}

extern "C" JNIEXPORT jstring JNICALL
Java_sentinel_kit_detector_EmulatorDetector_getEmulatorDetectionReason(JNIEnv *env, jobject thiz) {
  std::string reason = "";

  reason += checkPipesAndFiles();
  reason += checkEmulatorProperties();
  reason += checkCpuInfo();
  reason += checkExtraApps();

  if (reason.empty()) return nullptr;

  return env->NewStringUTF(reason.c_str());
}