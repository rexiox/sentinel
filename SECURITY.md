# Security Policy

## Supported Versions

Only the latest stable release of Sentinel receives security fixes.

| Version | Supported |
|---------|-----------|
| 1.8.x   | ✅ Yes     |
| < 1.8.0 | ❌ No      |

## Reporting a Vulnerability

**Please do NOT open a public GitHub issue for security vulnerabilities.**

If you discover a security vulnerability in Sentinel, please report it responsibly:

### Option 1 — GitHub Private Vulnerability Reporting (Preferred)

Use GitHub's built-in private reporting:
[Report a vulnerability](https://github.com/rexiox/sentinel/security/advisories/new)

### Option 2 — Email

Send details to the maintainer via the contact info on [rexiox.co](https://rexiox.co).

Please include the subject line: `[SECURITY] Sentinel vulnerability report`

---

## What to Include

A good vulnerability report includes:

- **Description** — what the vulnerability is and what impact it has
- **Affected versions** — which version(s) are affected
- **Steps to reproduce** — a minimal reproducible case
- **Suggested fix** (optional) — if you have one

---

## Scope

The following are **in scope**:

- Bypass techniques for any detector (root, jailbreak, hook, tamper, emulator, debug)
- Logic errors in `SecurityReport` severity scoring that could cause false confidence
- Memory safety issues in the native C/C++ layer (`sentinel-kit/ndk`, `sentinel-kit/kni`)
- Supply chain issues (malicious dependency, build artifact tampering)

The following are **out of scope**:

- Vulnerabilities in sample apps only (not in the library itself)
- Issues that require physical device access with no realistic attacker scenario
- Known limitations documented in the README (e.g., iOS mock location not supported)

---

## Known Security Considerations

Sentinel is a **detection** library, not a prevention library. By design:

- A sufficiently sophisticated attacker with root/jailbreak can potentially bypass detections
- Sentinel's code is open source and can be studied by adversaries — use obfuscation (e.g.,
  R8/ProGuard) in production
- Server-side validation (Play Integrity API, App Attest) should be used alongside Sentinel for
  defense in depth