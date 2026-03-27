#!/bin/bash

# Configuration
SDK_VERSION="13.0"
OUTPUT_DIR="./libs"
mkdir -p "$OUTPUT_DIR/device"
mkdir -p "$OUTPUT_DIR/sim"

INCLUDE_FLAGS=$(find . -type d -not -path '*/.*' -not -path './libs' | sed 's|^| -I|' | tr -d '\n')
SOURCES=$(find . -name "*.c" -not -path '*/.*')

echo "Starting Fixed Build Process..."

# iPhone Device (arm64)
echo "Compiling iPhoneOS (arm64)..."
clang -arch arm64 -isysroot $(xcrun --sdk iphoneos --show-sdk-path) \
      -miphoneos-version-min=$SDK_VERSION $INCLUDE_FLAGS -c $SOURCES || exit 1
ar rcs "$OUTPUT_DIR/device/libnative_detector.a" *.o
rm *.o

echo "Compiling Simulator Slices..."
# arm64 simulator
clang -arch arm64 -isysroot $(xcrun --sdk iphonesimulator --show-sdk-path) \
      -miphonesimulator-version-min=$SDK_VERSION $INCLUDE_FLAGS -c $SOURCES || exit 1
ar rcs "$OUTPUT_DIR/sim/libnative_detector_arm64.a" *.o
rm *.o

# x86_64 simulator
clang -arch x86_64 -isysroot $(xcrun --sdk iphonesimulator --show-sdk-path) \
      -miphonesimulator-version-min=$SDK_VERSION $INCLUDE_FLAGS -c $SOURCES || exit 1
ar rcs "$OUTPUT_DIR/sim/libnative_detector_x64.a" *.o
rm *.o

echo "Creating Universal Simulator Binary..."
lipo -create \
    "$OUTPUT_DIR/sim/libnative_detector_arm64.a" \
    "$OUTPUT_DIR/sim/libnative_detector_x64.a" \
    -output "$OUTPUT_DIR/sim/libnative_detector.a"

rm "$OUTPUT_DIR/sim/libnative_detector_arm64.a" "$OUTPUT_DIR/sim/libnative_detector_x64.a"

echo "Success"
echo "Device Lib: $OUTPUT_DIR/device/libnative_detector.a"
echo "Sim Lib: $OUTPUT_DIR/sim/libnative_detector.a"