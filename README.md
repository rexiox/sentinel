<div align="center">

<p align="center">
  <img src="/art/logo.png" alt="Logo" width="200"/>
</p>

[![Security](https://img.shields.io/badge/Security-000000?style=for-the-badge&logo=bitwarden)](#)
[![Toolkit](https://img.shields.io/badge/Toolkit-000000?style=for-the-badge&logo=hackthebox&logoColor=ffffff)](#)
[![Gradle](https://img.shields.io/badge/Gradle-000000?style=for-the-badge&logo=gradle)](#)
[![Version](https://img.shields.io/badge/1.2.0.alpha1-000000?style=for-the-badge&logo=stackblitz)](#)

[![KMP](https://img.shields.io/badge/Kotlin%20Multiplatform%20-000000?style=for-the-badge&logo=kotlin&logoColor=ffffff)](#)
[![Android](https://img.shields.io/badge/Android-000000?style=for-the-badge&logo=android&logoColor=ffffff)](https://developer.android.com/)
[![iOS](https://img.shields.io/badge/iOS-000000?style=for-the-badge&logo=apple&logoColor=ffffff)](https://developer.apple.com/)

**Sentinel** is a lightweight, modular Kotlin Multiplatform security toolkit designed to analyze
runtime environments and detect potential security threats in real time on both Android and iOS.

> **Note:** This library is currently under active development. Some features, especially on iOS may
> be incomplete or experimental.

<!--
<a href="https://play.google.com/store/apps/details?id=com.rs.sentinel.app">
    <img src="art/google-play-badge.png" width="200" alt="sentinel"/>
</a>
-->

<p align="center">
  <img alt="image" src="art/header.png" />
</p>

<table width="100%">
  <thead>
    <tr>
      <th width="50%" align="center">Android</th>
      <th width="50%" align="center">iOS</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">
        <img src="art/Android.gif" width="100%" style="max-width:300px;" alt="Sentinel Android Demo">
      </td>
      <td align="center">
        <img src="art/iOS.gif" width="100%" style="max-width:300px;" alt="Sentinel iOS Demo">
      </td>
    </tr>
  </tbody>
</table>
</div>

## Why Sentinel?
Most mobile apps rely only on server-side security, but attacks happen on the client.

Sentinel provides real-time, on-device threat detection with minimal performance overhead.

☑️️ Detect compromised devices (root / jailbreak)  
☑️️ Detect runtime manipulation (Frida, Xposed)  
☑️️ Detect app tampering & reverse engineering  
☑️️ Detect emulators & unsafe environments  
☑️️ Designed for Kotlin Multiplatform (KMP)  

## Features

♦️ **Modular Detector Architecture:** Easily enable, disable, or extend security checks.  
♦️ **Smart Risk Aggregation:** Weighted category scoring to prevent artificial risk inflation.  
♦️ **Configurable Threat Threshold:** Set your own critical risk level to control app behavior.  
♦️ **DSL-Based Configuration:** Use a clean and expressive API for configuration.  
♦️ **Detailed Security Reports:** Get a full breakdown of detected threats.  
♦️ **Lightweight & High Performance:** Minimal runtime overhead for optimal performance.  
♦️ **Kotlin Multiplatform:** Works on Android and iOS with a single codebase.

## Supported Threats by Platform

| Threat / Feature               | Android | iOS |
|--------------------------------|:-------:|:---:|
| Root / Jailbreak               |    ✅    |  ✅  |
| App Tampering                  |    ✅    |  ❌  |
| Hooking Detection              |    ✅    |  ✅  |
| Emulator / Simulator Detection |    ✅    |  ✅  |
| Debugging Detection            |    ✅    |  ✅  |
| Mock Location Abuse            |    ✅    |  ❌  |

## Quick Start

```gradle
implementation("io.github.resulsilay:sentinel:1.2.0-alpha1")
```

### Android Usage

```kotlin
val sentinel = Sentinel.configure(context = context) {
    config {
        this.appId = Sentinel.Identity.appId.toByteList()
        this.signature = Sentinel.Identity.signature?.toByteList()
        this.threshold = 90
        this.isLoggingEnabled = true
    }

    all()
    // root()
    // tamper()
    // hook()
    // emulator()
    // debug()
    // location()
}
```

### iOS Usage

```kotlin
val sentinel = Sentinel.configure {
    config {
        this.appId = Sentinel.Identity.appId.toByteList()
        this.threshold = 90
        this.isLoggingEnabled = true
    }

    all()
    // jailbreak()
    // hook()
    // simulator()
    // debug()
}
```

### Running Inspection

Instead of basic checks, Sentinel performs a thorough inspection of the environment and provides a
detailed report based on threat severity.

> `inspect()` is a suspend function and must be executed within a coroutine scope.

```kotlin
val report = sentinel.inspect()
```

### Report

After the inspection completes, Sentinel returns a `SecurityReport`.
This report aggregates all detected threats and provides a unified
severity score and risk level for the current runtime environment.

```kotlin
println("Risk Level: ${report.riskLevel}")
println("Total Risk Score: ${report.severity} / ${report.threshold}")
println("Threat Count: ${report.threats.size}")
println("Timestamp: ${report.timestamp}")

if (report.isRooted) println("Root detected")
if (report.isJailbroken) println("Jailbreak detected")
if (report.isTampered) println("App tampering detected")
if (report.isHooked) println("Hooking detected")
if (report.isEmulator) println("Emulator detected")
if (report.isSimulator) println("Simulator detected")
if (report.isDebugged) println("Debugger detected")
if (report.isMockLocation) println("Mock location detected")

if (report.isSafe()) {
    println("Device is secure")
} else {
    println("Security risks detected!")
}

if (report.isCritical()) {
    println("Block app usage.")
}
```

You can optionally log the report to the console / logcat for debugging purposes:

```kotlin
sentinel.log(report = report)
```

## Samples
- [Multiplatform](sample/multiplatform)
- [Android](sample/android)

## Risk Scoring
Sentinel does NOT simply sum threats.

Instead:
- Groups threats by category
- Takes the highest severity per category
- Produces a realistic risk score

## License

```
MIT License

Copyright (c) 2026 Resul Silay

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```