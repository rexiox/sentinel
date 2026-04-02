#ifndef TAMPER_H
#define TAMPER_H

#include <stdbool.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

bool verifyBundleId(const int8_t *bundleIdData, int32_t length);
bool verifyProvisioningHash(const int8_t *hashData, int32_t length);

#ifdef __cplusplus
}
#endif

#endif