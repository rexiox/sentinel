#ifndef HOOK_H
#define HOOK_H

#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

bool scanMemoryForFridaSignatures(void);
bool checkFridaDefaultPort(void);
bool checkDyldImages(void);
bool isFunctionHooked(void *func_ptr);

#ifdef __cplusplus
}
#endif

#endif