#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/stat.h>
#include <unistd.h>

#define MASK_KEY 0x55

void transform(char *str) {
  for (int i = 0; str[i] != '\0'; i++) {
    str[i] ^= MASK_KEY;
  }
}

bool pathExists(const char *maskedPath) {
  char path[256];
  strncpy(path, maskedPath, sizeof(path));
  transform(path);

  struct stat s;
  return (lstat(path, &s) == 0);
}

bool checkSandbox(void) {
  char path[] = {0x7A, 0x25, 0x27, 0x3C, 0x23, 0x34, 0x21, 0x30, 0x7A, 0x26,
                 0x30, 0x3B, 0x21, 0x3C, 0x3B, 0x30, 0x39, 0x0A, 0x3F, 0x34,
                 0x3C, 0x39, 0x37, 0x27, 0x30, 0x34, 0x3E, 0x0A, 0x21, 0x30,
                 0x26, 0x21, 0x7B, 0x21, 0x2D, 0x21, '\0'};
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
  const char *paths[] = {
      "\x7A\x19\x3C\x37\x27\x34\x27\x2C\x7A\x18\x3A\x37\x3C\x39\x30\x06\x20\x37"
      "\x26\x21\x27\x34\x21\x30\x7A\x18\x3A\x37\x3C\x39\x30\x06\x20\x37\x26\x21"
      "\x27\x34\x21\x30\x7E\x31\x2C\x39\x3C\x37",
      "\x7A\x37\x3C\x3B\x7A\x37\x34\x26\x3D",
      "\x7A\x20\x26\x27\x7A\x26\x3D\x34\x37\x30\x7A\x26\x26\x3D\x31",
      "\x7A\x30\x21\x36\x7A\x34\x25\x21",
      "\x7A\x25\x27\x3C\x23\x34\x21\x30\x7A\x23\x34\x27\x7A\x39\x3C\x37\x7A\x34\x25\x21\x7A",
      "\x7A\x23\x34\x27\x7A\x39\x3C\x37\x7A\x36\x2C\x31\x3C\x34",
      NULL};

  for (int i = 0; paths[i] != NULL; i++) {
    if (pathExists(paths[i])) {
      return true;
    }
  }

  return false;
}

bool checkSuspiciousSymlinks(void) {
  const char *links[] = {
      "\x7A\x14\x25\x25\x39\x3C\x36\x34\x21\x3C\x3A\x3B\x26",
      "\x7A\x20\x26\x27\x7A\x26\x3D\x34\x37\x30\x7A\x26\x3D\x34\x27\x30", NULL};

  for (int i = 0; links[i] != NULL; i++) {
    char path[256];
    strncpy(path, links[i], sizeof(path));
    transform(path);

    struct stat s;
    if (lstat(path, &s) == 0 && S_ISLNK(s.st_mode)) {
      return true;
    }
  }

  return false;
}

bool checkJailbreakApps(void) {
  const char *apps[] = {"\x7A\x14\x25\x25\x39\x3C\x36\x34\x21\x3C\x3A\x3B\x26"
                        "\x7A\x16\x2C\x31\x3C\x34\x7B\x34\x25\x25",
                        "\x7A\x14\x25\x25\x39\x3C\x36\x34\x21\x3C\x3A\x3B\x26"
                        "\x7A\x06\x3C\x39\x30\x3A\x7B\x34\x25\x25",
                        "\x7A\x14\x25\x25\x39\x3C\x36\x34\x21\x3C\x3A\x3B\x26"
                        "\x7A\x0F\x30\x37\x27\x34\x7B\x34\x25\x25",
                        NULL};

  for (int i = 0; apps[i] != NULL; i++) {
    if (pathExists(apps[i]))
      return true;
  }

  return false;
}