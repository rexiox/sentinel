#include <arpa/inet.h>
#include <dlfcn.h>
#include <mach-o/dyld.h>
#include <mach/mach.h>
#include <mach/mach_init.h>
#include <mach/mach_types.h>
#include <mach/task.h>
#include <mach/thread_act.h>
#include <netinet/in.h>
#include <os/log.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

__attribute__((always_inline)) bool scanMemoryForFridaSignatures() {
  vm_address_t address = 0;
  vm_size_t size = 0;
  kern_return_t kr;
  unsigned char buffer[4096];
  vm_size_t bytes_read;

  while (1) {
    vm_region_basic_info_data_64_t info;
    mach_msg_type_number_t count = VM_REGION_BASIC_INFO_COUNT_64;
    mach_port_t object;

    kr = vm_region_64(mach_task_self(), &address, &size, VM_REGION_BASIC_INFO_64,
                      (vm_region_info_t)&info, &count, &object);

    if (kr != KERN_SUCCESS) break;

    if ((info.protection & VM_PROT_READ) && !(info.protection & VM_PROT_EXECUTE)) {
      vm_size_t read_size = (size > sizeof(buffer)) ? sizeof(buffer) : size;

      if (vm_read_overwrite(mach_task_self(), address, read_size,
                            (vm_address_t)buffer, &bytes_read) == KERN_SUCCESS) {

        if (memmem(buffer, bytes_read, "frida", 5) ||
            memmem(buffer, bytes_read, "gum-js", 6) ||
            memmem(buffer, bytes_read, "GumScript", 9)) {
          return true;
        }
      }
    }

    address += size;
  }

  return false;
}

bool checkFridaDefaultPort() {
  struct sockaddr_in sa;
  sa.sin_family = AF_INET;
  sa.sin_port = htons(27042);
  inet_pton(AF_INET, "127.0.0.1", &sa.sin_addr);

  int sock = socket(AF_INET, SOCK_STREAM, 0);
  if (sock < 0)
    return false;

  int result = connect(sock, (struct sockaddr *)&sa, sizeof(sa));
  close(sock);

  return result == 0;
}

__attribute__((always_inline)) char* checkDyldImages(void) {
  uint32_t count = _dyld_image_count();
  for (uint32_t i = 0; i < count; i++) {
    const char *name = _dyld_get_image_name(i);
    if (!name)
      continue;

    if (strstr(name, "Frida") ||
        strstr(name, "frida") ||
        strstr(name, "cynject") ||
        strstr(name, "libhooker") ||
        strstr(name, "Substrate") ||
        strstr(name, "TweakInject")) {
      return strdup(name);
    }
  }

  return NULL;
}

bool isFunctionHooked(void *func_ptr) {
  if (!func_ptr) return false;

  Dl_info info;
  if (dladdr(func_ptr, &info) && info.dli_fname != NULL) {
    const char *fname = info.dli_fname;

    bool isSystemLib = strstr(fname, "/usr/lib/") ||
                       strstr(fname, "/System/Library/") ||
                       strstr(fname, "RuntimeRoot") ||
                       strstr(fname, "/Applications/Xcode") ||
                       strstr(fname, "/Developer/") ||
                       strstr(fname, "UIKit") ||
                       strstr(fname, "Foundation") ||
                       strstr(fname, "CoreFoundation");

    if (isSystemLib) {
      uint32_t first_instruction = *(uint32_t *)func_ptr;

      if ((first_instruction & 0xFC000000) == 0x14000000 ||
          first_instruction == 0x58000050) {
        return true;
      }
    }
  }

  return false;
}