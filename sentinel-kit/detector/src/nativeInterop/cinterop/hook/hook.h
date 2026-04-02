#ifndef HOOK_H
#define HOOK_H

#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

void setViolationHandler(void (*handler)(void));
bool scanMemorySignatures(void);
bool checkReservedPort(void);
bool verifyLoadedImages(void);
bool isInstructionTampered(void *func_ptr);

#ifdef __cplusplus
}
#endif

#endif