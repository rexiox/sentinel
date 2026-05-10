#include "../../obfuscate/sentinel-obfuscate.hpp"
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/stat.h>
#include <unistd.h>

bool pathExists(const char *maskedPath) {
  if (!maskedPath) return false;

  char path[256];
  memset(path, 0, sizeof(path));
  strncpy(path, maskedPath, sizeof(path) - 1);
  transform(path);
  struct stat s;
  return (lstat(path, &s) == 0);
}

bool checkSandbox(void) {
  if (!JAILBREAK_MOUNTS[0]) return false;

  char path[256];
  snprintf(path, sizeof(path), "%s", JAILBREAK_MOUNTS[0]);
  transform(path);

  FILE *f = fopen(path, "w");

  if (f != NULL) {
    fputs("sentinel_check", f);
    fclose(f);
    remove(path);
    return true;
  }

  return false;
}

bool checkSystemPaths(void) {
  for (int i = 0; JAILBREAK_BINARIES[i] != NULL; i++) {
    if (pathExists(JAILBREAK_BINARIES[i])) {
      return true;
    }
  }

  return false;
}

bool checkSuspiciousSymlinks(void) {
  for (int i = 0; JAILBREAK_DYLIBS[i] != NULL; i++) {
    char path[256];
    strncpy(path, JAILBREAK_DYLIBS[i], sizeof(path));
    transform(path);

    struct stat s;
    if (lstat(path, &s) == 0 && S_ISLNK(s.st_mode)) {
      return true;
    }
  }

  return false;
}

bool checkJailbreakApps(void) {
  for (int i = 0; JAILBREAK_PLISTS[i] != NULL; i++) {
    if (pathExists(JAILBREAK_PLISTS[i]))
      return true;
  }

  return false;
}