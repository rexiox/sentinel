#include <arpa/inet.h>
#include <dlfcn.h>
#include <mach-o/dyld.h>
#include <mach/mach.h>
#include <mach/mach_init.h>
#include <mach/mach_types.h>
#include <mach/task.h>
#include <mach/thread_act.h>
#include <netinet/in.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

static bool g_violation_reported = false;
static void (*violation_handler)(void) = NULL;

void setViolationHandler(void (*handler)(void)) { violation_handler = handler; }

static void reportViolation() {
  if (violation_handler) {
    violation_handler();
  }
}

bool isInstructionTampered(void *func_ptr) {
  if (!func_ptr)
    return false;

  Dl_info info;
  if (dladdr(func_ptr, &info) && info.dli_fname != NULL) {
    const char *fname = info.dli_fname;
    bool is_system = strstr(fname, "/usr/lib/") ||
                     strstr(fname, "/System/Library/") ||
                     strstr(fname, "RuntimeRoot");
    if (is_system) {
      uint32_t instruction = *(uint32_t *)func_ptr;
      if ((instruction & 0xFC000000) == 0x14000000 ||
          instruction == 0x58000050) {
        return true;
      }
    }
  }

  return false;
}

bool scanMemorySignatures() {
  vm_address_t addr = 0;
  vm_size_t size = 0;
  kern_return_t kr;
  unsigned char buf[4096];
  vm_size_t read_bytes;

  while (1) {
    vm_region_basic_info_data_64_t info;
    mach_msg_type_number_t count = VM_REGION_BASIC_INFO_COUNT_64;
    mach_port_t obj;
    kr = vm_region_64(mach_task_self(),
                      &addr, &size,
                      VM_REGION_BASIC_INFO_64,
                      (vm_region_info_t)&info,
                      &count,
                      &obj);
    if (kr != KERN_SUCCESS)
      break;
    if ((info.protection & VM_PROT_READ) &&
        !(info.protection & VM_PROT_EXECUTE)) {
      vm_size_t chunk = (size > sizeof(buf)) ? sizeof(buf) : size;
      if (vm_read_overwrite(
              mach_task_self(),
              addr,
              chunk,
              (vm_address_t)buf,
              &read_bytes) == KERN_SUCCESS) {
        if (memmem(buf, read_bytes, "frida", 5) ||
            memmem(buf, read_bytes, "gum-js", 6) ||
            memmem(buf, read_bytes, "GumScript", 9))
          return true;
      }
    }
    addr += size;
  }
  return false;
}

bool checkReservedPort() {
  struct sockaddr_in sa;
  sa.sin_family = AF_INET;
  sa.sin_port = htons(27042);
  inet_pton(AF_INET, "127.0.0.1", &sa.sin_addr);
  int fd = socket(AF_INET, SOCK_STREAM, 0);
  if (fd < 0)
    return false;

  struct timeval timeout = {.tv_sec = 0, .tv_usec = 500000};
  setsockopt(fd, SOL_SOCKET, SO_SNDTIMEO, &timeout, sizeof(timeout));
  int res = connect(fd, (struct sockaddr *)&sa, sizeof(sa));
  close(fd);
  return res == 0;
}

bool verifyLoadedImages() {
  uint32_t total = _dyld_image_count();
  for (uint32_t i = 0; i < total; i++) {
    const char *path = _dyld_get_image_name(i);
    if (path && (strstr(path, "Frida") ||
                 strstr(path, "frida") ||
                 strstr(path, "gadget") ||
                 strstr(path, "cynject") ||
                 strstr(path, "libhooker")))
      return true;
  }
  return false;
}

bool checkSymbolIntegrity() {
  void *symbols[] = {dlsym(RTLD_DEFAULT, "ptrace"),
                     dlsym(RTLD_DEFAULT, "dlopen"),
                     dlsym(RTLD_DEFAULT, "exit"),
                     dlsym(RTLD_DEFAULT, "objc_msgSend")};

  for (int i = 0; i < 4; i++) {
    if (symbols[i] && isInstructionTampered(symbols[i])) {
      return true;
    }
  }
  return false;
}

void *integrity_monitor(void *arg) {
  while (true) {
    bool has_violation = verifyLoadedImages() ||
                         checkReservedPort() ||
                         scanMemorySignatures() ||
                         checkSymbolIntegrity();

    if (has_violation && !g_violation_reported) {
      reportViolation();
      g_violation_reported = true;
    }
    else if (!has_violation && g_violation_reported) {
      g_violation_reported = false;
    }

    sleep(3);
  }

  return NULL;
}

static void image_load_handler(const struct mach_header *mh, intptr_t slide) {
  Dl_info info;
  if (dladdr(mh, &info) && info.dli_fname) {
    if (strstr(info.dli_fname, "frida") ||
        strstr(info.dli_fname, "gadget"))
      reportViolation();
  }
}

__attribute__((constructor)) static void init() {
  _dyld_register_func_for_add_image(image_load_handler);
  pthread_t thread_id;
  pthread_create(&thread_id, NULL, integrity_monitor, NULL);
}