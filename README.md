<div align="center">

<p align="center">
  <img src="/art/logo.png" alt="Logo" width="200"/>
</p>

[![Security](https://img.shields.io/badge/Security-000000?style=for-the-badge&logo=bitwarden)](#)
[![KMP](https://img.shields.io/badge/KMP-000000?style=for-the-badge&logo=kotlin&logoColor=ffffff)](#)
[![Android](https://img.shields.io/badge/Android-000000?style=for-the-badge&logo=android&logoColor=ffffff)](https://developer.android.com/)
[![iOS](https://img.shields.io/badge/iOS-000000?style=for-the-badge&logo=apple&logoColor=ffffff)](https://developer.apple.com/)
[![Version](https://img.shields.io/badge/1.4.2.alpha-000000?style=for-the-badge&logo=stackblitz)](#)

**Sentinel** is a lightweight, modular Kotlin Multiplatform security toolkit designed to analyze
runtime environments and detect potential security threats in real time on both Android and iOS.

<p align="center">
  <img alt="image" src="art/header.png" />
</p>
</div>

## Why Sentinel?

Most mobile apps rely only on server-side security, but attacks happen on the client.

Sentinel provides real-time, on-device threat detection with minimal performance overhead.

☑️️ Detect compromised devices (root / jailbreak)  
☑️️ Detect runtime manipulation (Frida, Xposed)  
☑️️ Detect app tampering & reverse engineering  
☑️️ Detect emulators & unsafe environments  
☑️️ Designed for Kotlin Multiplatform (KMP)


<table style="width: auto; margin: auto; border-collapse: collapse;">
  <thead>
    <tr>
      <th align="center" style="padding: 10px;">Android</th>
      <th align="center" style="padding: 10px;">iOS</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center" style="padding: 5px;">
        <img src="art/Android.gif" width="240" alt="Android Demo">
      </td>
      <td align="center" style="padding: 5px;">
        <img src="art/iOS.gif" width="240" alt="iOS Demo">
      </td>
    </tr>
    <tr>
      <td align="center" style="padding: 10px;">
        <a href="https://play.google.com/store/apps/details?id=com.rs.sentinel">
          <img src="art/google-play-badge.svg" height="40" alt="Google Play">
        </a>
      </td>
      <td align="center" style="padding: 10px;">
        <a href="https://apps.apple.com/us/app/">
          <img src="art/app-store-badge.svg" height="40" alt="App Store">
        </a>
      </td>
    </tr>
  </tbody>
</table>

## Features

♦️ **Kotlin Multiplatform:** Works on Android and iOS with a single codebase.  
♦️ **Modular Detector Architecture:** Easily enable, disable, or extend security checks.  
♦️ **Smart Risk Aggregation:** Weighted category scoring to prevent artificial risk inflation.  
♦️ **Configurable Threat Threshold:** Set your own critical risk level to control app behavior.  
♦️ **DSL-Based Configuration:** Use a clean and expressive API for configuration.  
♦️ **Detailed Security Reports:** Get a full breakdown of detected threats.  
♦️ **Lightweight & High Performance:** Minimal runtime overhead for optimal performance.  
♦️ **RASP-Based Threat Detection:** Real-time monitoring of dynamic instrumentation, hooking, and
injection attempts.

## Supported Threats by Platform

| Threat / Feature               | Android | iOS |
|--------------------------------|:-------:|:---:|
| Root / Jailbreak               |    ✅    |  ✅  |
| Tamper Detection               |    ✅    |  ✅  |
| Hooking Detection              |    ✅    |  ✅  |
| Emulator / Simulator Detection |    ✅    |  ✅  |
| Debugging Detection            |    ✅    |  ✅  |
| Mock Location Abuse            |    ✅    |  ➖  |

## Quick Start

```gradle
implementation("io.github.resulsilay:sentinel:1.4.2-alpha")
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
        this.hash = Sentinel.Identity.hash?.toByteList()
        this.threshold = 90
        this.isLoggingEnabled = true
    }

    all()
    // jailbreak()
    // tamper()
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
SentinelLogger.report(report = report)
```

### RASP - Runtime Application Self-Protection (Detection)

Sentinel extends protection beyond initial startup checks by continuously monitoring the application
during runtime. Through background scanning mechanisms, it detects unauthorized access attempts,
runtime tampering, and external manipulation activities in real time.

```kotlin
sentinel.runtime {
    onCompromised {
        info(msg = "Device integrity failed (Root/Jailbreak detected).")
    }

    onTampered {
        info(msg = "App tampering detected.")
    }

    onHooked {
        info(msg = "Runtime hook detection.")
    }

    onSimulated {
        info(msg = "Running on Emulator/Simulator environment.")
    }

    onDebugged {
        info(msg = "Active debugging session detected.")
    }

    onCritical { score ->
        info(msg = "High risk score reached: $score")
    }

    onSafe {
        info(msg = "All systems nominal.")
    }
}
```

### Sentinel Monitor

Sentinel Monitor is a monitoring infrastructure that tracks application-layer vulnerabilities and
suspicious activities in real-time and reports these threats.

```gradle
implementation("io.github.resulsilay:sentinel-monitor:1.4.2-alpha")
```

```kotlin
SentinelMonitor.start(
    appId = sentinel.config.appId.orEmpty(),
    hash = sentinel.config.hash.orEmpty(),
    threshold = sentinel.config.threshold
)
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