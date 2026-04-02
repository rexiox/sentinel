#include <CommonCrypto/CommonDigest.h>
#include <CoreFoundation/CoreFoundation.h>
#include <fcntl.h>
#include <os/log.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

bool verifyBundleId(const int8_t *bytes, int length) {
  if (!bytes || length <= 0)
    return false;

  CFBundleRef mainBundle = CFBundleGetMainBundle();
  if (!mainBundle)
    return false;

  CFStringRef bundleIDRef = CFBundleGetIdentifier(mainBundle);
  if (!bundleIDRef)
    return false;

  char current_id[256];
  if (!CFStringGetCString(bundleIDRef, current_id, sizeof(current_id),
                          kCFStringEncodingUTF8)) {
    return false;
  }

  return (strlen(current_id) == (size_t)length) &&
         (memcmp(current_id, bytes, length) == 0);
}

bool verifyProvisioningHash(const int8_t *bytes, int length) {
  if (!bytes || length <= 0)
    return false;

  char path[1024];
  CFBundleRef mainBundle = CFBundleGetMainBundle();
  CFURLRef resourcesURL = CFBundleCopyResourcesDirectoryURL(mainBundle);
  CFURLGetFileSystemRepresentation(resourcesURL, true, (UInt8 *)path, 1024);
  CFRelease(resourcesURL);
  strcat(path, "/embedded.mobileprovision");

  int fd = open(path, O_RDONLY);
  if (fd < 0)
    return false;

  CC_SHA256_CTX sha256;
  CC_SHA256_Init(&sha256);
  uint8_t buffer[4096];
  ssize_t bytes_read;
  while ((bytes_read = read(fd, buffer, sizeof(buffer))) > 0) {
    CC_SHA256_Update(&sha256, buffer, (CC_LONG)bytes_read);
  }
  close(fd);

  uint8_t hash[CC_SHA256_DIGEST_LENGTH];
  CC_SHA256_Final(hash, &sha256);

  char hex_hash[CC_SHA256_DIGEST_LENGTH * 2 + 1];
  for (int i = 0; i < CC_SHA256_DIGEST_LENGTH; i++) {
    sprintf(&hex_hash[i * 2], "%02x", hash[i]);
  }

  return (strlen(hex_hash) == (size_t)length) &&
         (memcmp(hex_hash, bytes, length) == 0);
}