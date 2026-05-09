# Benchmark Performance Report

> **Generated on:** 2026-05-09 17:34:18

> **Version:** v1.8.4.beta

## Device Information

| Property | Value |
| :--- | :--- |
| **Device** | google sdk_gphone16k_arm64 |
| **Android** | API 37 |
| **CPU** | 4 cores @ 0 MHz |
| **Memory** | 2.9 GB RAM |

---

### Runtime Performance (Inspect Only)

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_SequentialModules | Latency (ms) | 50 |0.64 | **0.65** | 0.71 | 0.68 | 🟢 | `█` |
| EMULATOR_SequentialModules | Alloc (count) | 5 |393.03 | **393.05** | 393.05 | 393.05 |  | `███` |
| EMULATOR_EmulatorOnly | Latency (ms) | 50 |0.01 | **0.01** | 0.02 | 0.01 | 🟢 | `█` |
| EMULATOR_EmulatorOnly | Alloc (count) | 5 |22.00 | **22.00** | 22.00 | 22.00 |  | `█` |
| EMULATOR_LocationOnly | Latency (ms) | 50 |0.34 | **0.34** | 0.39 | 0.36 | 🟢 | `█` |
| EMULATOR_LocationOnly | Alloc (count) | 5 |204.06 | **204.06** | 204.11 | 204.11 |  | `██` |
| EMULATOR_AllModules | Latency (ms) | 50 |1.04 | **1.05** | 1.17 | 1.15 | 🟢 | `██` |
| EMULATOR_AllModules | Alloc (count) | 5 |592.11 | **592.11** | 592.11 | 592.11 |  | `█████` |
| EMULATOR_HookOnly | Latency (ms) | 50 |0.58 | **0.59** | 0.62 | 0.60 | 🟢 | `█` |
| EMULATOR_HookOnly | Alloc (count) | 5 |319.01 | **319.03** | 319.03 | 319.03 |  | `███` |
| EMULATOR_DebugOnly | Latency (ms) | 50 |0.00 | **0.00** | 0.00 | 0.00 | 🟢 | `█` |
| EMULATOR_DebugOnly | Alloc (count) | 5 |18.00 | **18.00** | 18.00 | 18.00 |  | `█` |
| EMULATOR_RootOnly | Latency (ms) | 50 |0.05 | **0.06** | 0.06 | 0.06 | 🟢 | `█` |
| EMULATOR_RootOnly | Alloc (count) | 5 |66.01 | **66.01** | 66.01 | 66.01 |  | `█` |
| EMULATOR_TamperOnly | Latency (ms) | 50 |0.01 | **0.01** | 0.01 | 0.01 | 🟢 | `█` |
| EMULATOR_TamperOnly | Alloc (count) | 5 |38.00 | **38.00** | 38.00 | 38.00 |  | `█` |

### Cold Start Performance

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_CS_Hook | Latency (ms) | 50 |0.61 | **0.62** | 0.74 | 0.64 | 🟢 | `█` |
| EMULATOR_CS_Hook | Alloc (count) | 5 |409.03 | **409.03** | 409.05 | 409.05 |  | `████` |
| EMULATOR_CS_Root | Latency (ms) | 50 |0.09 | **0.09** | 0.11 | 0.10 | 🟢 | `█` |
| EMULATOR_CS_Root | Alloc (count) | 5 |156.03 | **156.03** | 156.03 | 156.03 |  | `█` |
| EMULATOR_CS_All | Latency (ms) | 50 |1.10 | **1.12** | 1.85 | 1.23 | 🟢 | `██` |
| EMULATOR_CS_All | Alloc (count) | 5 |764.14 | **764.14** | 764.19 | 764.19 |  | `███████` |
| EMULATOR_CS_Tamper | Latency (ms) | 50 |0.04 | **0.04** | 0.04 | 0.04 | 🟢 | `█` |
| EMULATOR_CS_Tamper | Alloc (count) | 5 |127.02 | **127.02** | 127.02 | 127.02 |  | `█` |
| EMULATOR_CS_Emulator | Latency (ms) | 50 |0.04 | **0.05** | 0.05 | 0.05 | 🟢 | `█` |
| EMULATOR_CS_Emulator | Alloc (count) | 5 |111.01 | **111.01** | 111.02 | 111.02 |  | `█` |
| EMULATOR_CS_Location | Latency (ms) | 50 |0.37 | **0.38** | 0.41 | 0.39 | 🟢 | `█` |
| EMULATOR_CS_Location | Alloc (count) | 5 |275.09 | **275.09** | 275.10 | 275.10 |  | `██` |
| EMULATOR_CS_Debug | Latency (ms) | 50 |0.03 | **0.03** | 0.04 | 0.04 | 🟢 | `█` |
| EMULATOR_CS_Debug | Alloc (count) | 5 |107.01 | **107.01** | 107.02 | 107.02 |  | `█` |
| EMULATOR_CS_Sequential | Latency (ms) | 50 |0.69 | **0.69** | 0.73 | 0.70 | 🟢 | `█` |
| EMULATOR_CS_Sequential | Alloc (count) | 5 |522.14 | **522.14** | 522.18 | 522.18 |  | `█████` |

### Initialization Only

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_Init_Hook | Latency (ms) | 50 |0.03 | **0.03** | 0.03 | 0.03 | 🟢 | `█` |
| EMULATOR_Init_Hook | Alloc (count) | 5 |89.01 | **89.01** | 89.01 | 89.01 |  | `█` |
| EMULATOR_Init_Root | Latency (ms) | 50 |0.03 | **0.03** | 0.03 | 0.03 | 🟢 | `█` |
| EMULATOR_Init_Root | Alloc (count) | 5 |89.01 | **89.01** | 89.01 | 89.01 |  | `█` |
| EMULATOR_Init_Tamper | Latency (ms) | 50 |0.03 | **0.03** | 0.03 | 0.03 | 🟢 | `█` |
| EMULATOR_Init_Tamper | Alloc (count) | 5 |89.01 | **89.02** | 89.02 | 89.02 |  | `█` |
| EMULATOR_Init_Debug | Latency (ms) | 50 |0.03 | **0.03** | 0.03 | 0.03 | 🟢 | `█` |
| EMULATOR_Init_Debug | Alloc (count) | 5 |89.01 | **89.01** | 89.01 | 89.01 |  | `█` |
| EMULATOR_Init_All | Latency (ms) | 50 |0.05 | **0.05** | 0.06 | 0.06 | 🟢 | `█` |
| EMULATOR_Init_All | Alloc (count) | 5 |171.03 | **171.03** | 171.03 | 171.03 |  | `█` |
| EMULATOR_Init_Emulator | Latency (ms) | 50 |0.03 | **0.03** | 0.03 | 0.03 | 🟢 | `█` |
| EMULATOR_Init_Emulator | Alloc (count) | 5 |89.01 | **89.01** | 89.01 | 89.01 |  | `█` |
| EMULATOR_Init_Sequential | Latency (ms) | 50 |0.04 | **0.04** | 0.04 | 0.04 | 🟢 | `█` |
| EMULATOR_Init_Sequential | Alloc (count) | 5 |129.02 | **129.02** | 129.02 | 129.02 |  | `█` |
| EMULATOR_Init_Location | Latency (ms) | 50 |0.02 | **0.02** | 0.02 | 0.02 | 🟢 | `█` |
| EMULATOR_Init_Location | Alloc (count) | 5 |71.01 | **71.01** | 71.01 | 71.01 |  | `█` |

### Memory Allocation Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_Mem_AllocationMultiple | Latency (ms) | 50 |5.44 | **5.54** | 6.33 | 6.22 | 🟢 | `███` |
| EMULATOR_Mem_AllocationMultiple | Alloc (count) | 5 |3792.11 | **3792.11** | 3792.44 | 3792.44 |  | `██████████` |
| EMULATOR_Mem_AllocationSingle | Latency (ms) | 50 |1.09 | **1.13** | 1.48 | 1.25 | 🟢 | `██` |
| EMULATOR_Mem_AllocationSingle | Alloc (count) | 5 |764.22 | **764.29** | 764.36 | 764.36 |  | `███████` |
| EMULATOR_Mem_PressureHeavy | Latency (ms) | 50 |2.01 | **2.05** | 2.29 | 2.09 | 🟢 | `██` |
| EMULATOR_Mem_PressureHeavy | Alloc (count) | 5 |1915.51 | **1915.51** | 1915.75 | 1915.75 |  | `██████████` |

### Instance Reuse Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_ReuseSingle | Latency (ms) | 50 |1.04 | **1.05** | 1.10 | 1.07 | 🟢 | `██` |
| EMULATOR_ReuseSingle | Alloc (count) | 5 |592.20 | **592.20** | 592.32 | 592.32 |  | `█████` |
| EMULATOR_ReuseInstance | Latency (ms) | 50 |10.34 | **10.52** | 11.21 | 10.89 | 🟢 | `████` |
| EMULATOR_ReuseInstance | Alloc (count) | 5 |5849.56 | **5849.56** | 5850.22 | 5850.22 |  | `██████████` |

### Edge Cases

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_AllModulesConcurrent | Latency (ms) | 50 |3.10 | **3.14** | 3.24 | 3.21 | 🟢 | `██` |
| EMULATOR_AllModulesConcurrent | Alloc (count) | 5 |1760.44 | **1760.44** | 1760.66 | 1760.66 |  | `██████████` |
| EMULATOR_EmptyConfiguration | Latency (ms) | 50 |0.02 | **0.02** | 0.02 | 0.02 | 🟢 | `█` |
| EMULATOR_EmptyConfiguration | Alloc (count) | 5 |83.01 | **83.01** | 83.01 | 83.01 |  | `█` |

### Manual Warmup Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| EMULATOR_WithManualWarmup | Latency (ms) | 50 |1.03 | **1.05** | 1.08 | 1.08 | 🟢 | `██` |
| EMULATOR_WithManualWarmup | Alloc (count) | 5 |592.15 | **592.22** | 592.22 | 592.22 |  | `█████` |

## Statistical Analysis

| Metric | Value |
| :--- | :--- |
| **Total Tests** | 32 |
| **Average Latency** | 0.95 ms |
| **Median (P50)** | 0.09 ms |
| **P95 Latency** | 5.54 ms |
| **P99 Latency** | 10.52 ms |
| **Fastest Test** | 0.00 ms |
| **Slowest Test** | 10.52 ms |
| **Std Deviation** | 2.05 ms |
| **Coeff. of Variation** | 214.87% |

### Performance Distribution

| Status | Count | Percentage |
| :--- | :---: | :---: |
| 🟢 Excellent (< 20ms) | 32 | 100.0% |
| 🟡 Good (20-50ms) | 0 | 0.0% |
| 🟠 Acceptable (50-100ms) | 0 | 0.0% |
| 🔴 Critical (> 100ms) | 0 | 0.0% |

## Performance Evaluation Reference

Visual indicators are based on Android performance best practices and human perception thresholds.

| Status | Indicator | Detection Time | Basis | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Excellent** | 🟢 | < 20ms | **Imperceptible** | Near-instant detection with zero user impact. |
| **Good** | 🟡 | 20ms – 50ms | **Fast Enough** | Quick validation suitable for production use. |
| **Acceptable** | 🟠 | 50ms – 100ms | **Perception Threshold** | Noticeable but tolerable delay. |
| **Critical** | 🔴 | > 100ms | **Human Perception** | Delay becomes disruptive to user experience. |


## Notes

- **Inspect Only**: Tests measure runtime performance of already-initialized instances
- **Cold Start**: Tests include both initialization and first inspection
- **Initialization Only**: Tests measure only the setup/configuration overhead
- **P95**: 95% of all measurements are below this value (excludes outliers)
- **CV**: Coefficient of Variation - lower is better (< 20% is excellent)

