# Contributing to Sentinel

Sentinel is a Kotlin Multiplatform security library focused on building secure, reliable, and
production-ready applications across platforms.

Contributions are welcome in many forms, including bug reports, feature proposals, documentation
improvements, performance enhancements, and code contributions. Every contribution helps improve the
quality, security, and maintainability of the project.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Running Tests](#running-tests)
- [Security Vulnerabilities](#security-vulnerabilities)

---

## Code of Conduct

Be respectful, constructive, and collaborative. We follow
the [Contributor Covenant](https://www.contributor-covenant.org/version/2/1/code_of_conduct/).

---

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/rexiox/sentinel.git`
3. Create a branch: `git checkout -b feat/branch`
4. Make your changes
5. Open a Pull Request against `main`

---

## Project Structure

```
sentinel/
├── sentinel-core/        # Shared models: SecurityReport, RiskLevel, Threat, Violation types
├── sentinel-kit/
│   ├── detector/         # Platform-specific detector implementations + tests
│   │   ├── androidMain/  # Android detectors (Root, Tamper, Hook, Emulator, Debug, Location)
│   │   ├── iosMain/      # iOS detectors (Jailbreak, Tamper, Hook, Simulator, Debug)
│   │   ├── androidUnitTest/  # Android unit tests (Robolectric)
│   │   └── iosTest/      # iOS tests (Mokkery mocks)
│   ├── ndk/              # Native C/C++ layer for low-level detection
│   └── kni/              # Kotlin/Native interop for iOS
├── sentinel-identity/    # appId / appIntegrity helpers
├── sentinel-runtime/     # RASP continuous monitoring
├── sentinel-monitor/     # Application-layer monitoring UI
├── sentinel-ui/          # Compose Multiplatform dashboard components
├── sentinel/             # Public API facade (Sentinel.configure { })
└── sample/               # Android and Multiplatform sample apps
```

---

## How to Contribute

### Bug Reports

Open a [GitHub Issue](https://github.com/rexiox/sentinel/issues/new) with:

- Sentinel version
- Target platform (Android / iOS) and OS version
- Steps to reproduce
- Expected vs actual behavior
- Relevant logs or stack traces

### Feature Requests

Open a [GitHub Discussion](https://github.com/rexiox/sentinel/discussions/new?category=ideas)
describing:

- The security gap or use case
- How you'd expect the API to look

### Documentation

Documentation improvements (README, code comments, wiki) are always welcome. Even fixing typos
counts!

### Adding a New Detector

1. Create the detector class in `sentinel-kit/detector/src/[platform]Main/`
2. Extend `SecurityDetector` and implement `detect(): List<Threat>`
3. Add corresponding `Violation` sealed class entries in `sentinel-core`
4. Wire it into the `Sentinel.configure { }` DSL in the `sentinel` module
5. Write unit tests in `androidUnitTest` or `iosTest`
6. Update the README security coverage table

---

## Development Setup

**Requirements:**

- JDK 17+
- Android Studio Ladybug or later (for Android targets)
- Xcode 16+ (for iOS targets, macOS only)
- Kotlin 2.x

**Build:**

```bash
./gradlew build
```

**Sync dependencies:**

```bash
./gradlew dependencies
```

---

## Running Tests

**Android unit tests (Robolectric):**

```bash
./gradlew :sentinel-kit:detector:testDebugUnitTest
```

**All checks:**

```bash
./gradlew check
```

**iOS tests** require a macOS machine with Xcode:

```bash
./gradlew :sentinel-kit:detector:iosSimulatorArm64Test
```

### Writing Tests

- Android tests use **Robolectric** and JUnit4
- iOS tests use **Mokkery** for mocking and `kotlin.test`
- Common (platform-agnostic) logic tests go in `commonTest`
- Every new detector must have corresponding tests
- Aim for both the "clean device" case and the "threat detected" case

---

### Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
feat(android): add Magisk detection to RootDetector
fix(ios): handle nil bundle ID in TamperDetector
docs: update README with Play Integrity guidance
test(android): add MockLocationSettingDetector tests
```

---

## Security Vulnerabilities

Please **do not** open public issues for security vulnerabilities. See [SECURITY.md](SECURITY.md)
for the responsible disclosure process.
