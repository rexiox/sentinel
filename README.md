<div align="center">

<p align="center">
  <img src="/art/logo.png" alt="Logo" width="200"/>
</p>

[![Android](https://img.shields.io/badge/Android-000000?style=for-the-badge&logo=android&logoColor=ffffff)](https://developer.android.com/)
[![Security](https://img.shields.io/badge/Security-000000?style=for-the-badge&logo=bitwarden)](#)
[![Toolkit](https://img.shields.io/badge/Toolkit-000000?style=for-the-badge&logo=hackthebox&logoColor=ffffff)](#)
[![Gradle](https://img.shields.io/badge/Gradle-000000?style=for-the-badge&logo=gradle)](#)
[![Version](https://img.shields.io/badge/1.2.2.beta-000000?style=for-the-badge&logo=stackblitz)](#)

Lightweight Android Security Toolkit for protecting apps against tampering, reverse engineering,
rooted devices, and insecure runtime environments.

<!--
<a href="https://play.google.com/store/apps/details?id=com.rs.sentinel.app">
    <img src="art/google-play-badge.png" width="200" alt="sentinel"/>
</a>
-->

<p align="center">
  <img alt="image" src="art/header.png" />
</p>

</div>
<br>

## Sentinel

**Sentinel** is a lightweight, modular Android security toolkit designed to analyze runtime
environments and detect potential security threats in real time.

It helps protect your application against:

- Rooted devices
- App tampering
- Hooking frameworks
- Emulators
- Debugging sessions
- Mock location abuse

Sentinel performs deep environmental inspection, calculates a unified **risk severity score**, and
produces a comprehensive security report.

## Features

♦️ **Modular Detector Architecture:** Easily enable, disable, or extend security checks.  
♦️ **Unified Risk Scoring System:** Aggregate all threats into a single severity score.  
♦️ **Configurable Threat Threshold:** Set your own critical risk level to control app behavior.  
♦️ **DSL-Based Configuration:** Use a clean and expressive API for configuration.  
♦️ **Detailed Security Reports:** Get a full breakdown of detected threats.  
♦️ **Lightweight & High Performance:** Minimal runtime overhead for optimal performance.

## Getting Started

Sentinel uses a centralized DSL configuration to manage all security checks.

```gradle
implementation("com.github.ResulSilay:Sentinel:1.2.2.jitpack.beta")
```

```kotlin
val sentinel = Sentinel.configure(context = context) {
    /* config {
        this.packageName = packageName.toByteList()
        this.packageSignature = packageSignature.toByteList()
        this.threshold = 80
    } */

    all()
    // root()
    // tamper()
    // hook()
    // emulator()
    // debug()
    // location()
}
```

Instead of basic checks, Sentinel performs a thorough inspection of the environment and provides a
detailed report based on threat severity.

```kotlin
val report = sentinel.inspect()

println("----- Security Report -----")
println("Risk Level: ${report.riskLevel}")
println("Severity Score: ${report.severity}")
println("Threat Count: ${report.threats.size}")
println("Timestamp: ${report.timestamp}")

if (report.isRooted) println("❌ Root detected")
if (report.isTampered) println("❌ App tampering detected")
if (report.isHooked) println("❌ Hooking detected")
if (report.isEmulator) println("❌ Emulator detected")
if (report.isDebuggable) println("❌ Debugger detected")
if (report.isMockLocation) println("❌ Mock location detected")

if (report.isSafe()) {
    println("✅ Device is secure")
} else {
    println("⚠️ Security risks detected!")
}

if (report.isCritical()) {
    println("🚫 Block app usage.")
}
```

## Installation

Add JitPack repository:

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Include the library in your **app module** `build.gradle`:

```gradle
implementation("com.github.ResulSilay:Sentinel:1.2.2.jitpack.beta")
```