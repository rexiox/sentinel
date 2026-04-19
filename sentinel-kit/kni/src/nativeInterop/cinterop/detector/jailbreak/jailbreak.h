#ifndef JAILBREAK_H
#define JAILBREAK_H

#include <stdbool.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

bool checkSandbox(void);
bool checkSystemPaths(void);
bool checkSuspiciousSymlinks(void);
bool checkJailbreakApps(void);

#ifdef __cplusplus
}
#endif

#endif