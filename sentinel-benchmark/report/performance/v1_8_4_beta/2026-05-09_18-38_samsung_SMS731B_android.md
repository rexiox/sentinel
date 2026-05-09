# Benchmark Performance Report

> **Generated on:** 2026-05-09 18:38:42

> **Version:** v1.8.4.beta

## Device Information

| Property | Value |
| :--- | :--- |
| **Device** | samsung SM-S731B |
| **Android** | API 36 |
| **CPU** | 10 cores @ 3207 MHz |
| **Memory** | 7.0 GB RAM |

---

### Runtime Performance (Inspect Only)

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| SequentialModules | Latency (ms) | 50 |1.78 | **1.80** | 1.84 | 1.82 | 🟢 | `██` |
| SequentialModules | Alloc (count) | 5 |384.04 | **384.04** | 384.04 | 384.04 |  | `███` |
| EmulatorOnly | Latency (ms) | 50 |0.17 | **0.18** | 0.21 | 0.20 | 🟢 | `█` |
| EmulatorOnly | Alloc (count) | 5 |15.00 | **15.00** | 15.00 | 15.00 |  | `█` |
| LocationOnly | Latency (ms) | 50 |0.75 | **1.29** | 1.55 | 1.45 | 🟢 | `██` |
| LocationOnly | Alloc (count) | 5 |200.02 | **200.02** | 200.07 | 200.07 |  | `██` |
| AllModules | Latency (ms) | 50 |2.97 | **3.18** | 4.60 | 3.41 | 🟢 | `██` |
| AllModules | Alloc (count) | 5 |572.07 | **572.07** | 572.30 | 572.30 |  | `█████` |
| HookOnly | Latency (ms) | 50 |1.82 | **1.83** | 1.87 | 1.85 | 🟢 | `██` |
| HookOnly | Alloc (count) | 5 |310.04 | **310.04** | 310.11 | 310.11 |  | `███` |
| DebugOnly | Latency (ms) | 50 |0.01 | **0.01** | 0.01 | 0.01 | 🟢 | `█` |
| DebugOnly | Alloc (count) | 5 |18.00 | **18.00** | 18.00 | 18.00 |  | `█` |
| RootOnly | Latency (ms) | 50 |0.15 | **0.16** | 0.20 | 0.17 | 🟢 | `█` |
| RootOnly | Alloc (count) | 5 |66.00 | **66.00** | 66.01 | 66.01 |  | `█` |
| TamperOnly | Latency (ms) | 50 |0.02 | **0.02** | 0.02 | 0.02 | 🟢 | `█` |
| TamperOnly | Alloc (count) | 5 |38.00 | **38.00** | 38.00 | 38.00 |  | `█` |

### Cold Start Performance

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| CS_Hook | Latency (ms) | 50 |1.47 | **1.49** | 1.53 | 1.52 | 🟢 | `██` |
| CS_Hook | Alloc (count) | 5 |400.03 | **400.03** | 400.21 | 400.21 |  | `████` |
| CS_Root | Latency (ms) | 50 |0.17 | **0.18** | 0.19 | 0.19 | 🟢 | `█` |
| CS_Root | Alloc (count) | 5 |156.00 | **156.01** | 156.01 | 156.01 |  | `█` |
| CS_All | Latency (ms) | 50 |2.88 | **2.92** | 4.23 | 2.99 | 🟢 | `██` |
| CS_All | Alloc (count) | 5 |744.06 | **744.06** | 744.27 | 744.27 |  | `███████` |
| CS_Tamper | Latency (ms) | 50 |0.08 | **0.08** | 0.08 | 0.08 | 🟢 | `█` |
| CS_Tamper | Alloc (count) | 5 |127.00 | **127.01** | 127.01 | 127.01 |  | `█` |
| CS_Emulator | Latency (ms) | 50 |0.24 | **0.26** | 0.30 | 0.29 | 🟢 | `█` |
| CS_Emulator | Alloc (count) | 5 |104.01 | **104.01** | 104.02 | 104.02 |  | `█` |
| CS_Location | Latency (ms) | 50 |0.84 | **1.38** | 2.09 | 1.51 | 🟢 | `██` |
| CS_Location | Alloc (count) | 5 |271.03 | **271.03** | 271.08 | 271.08 |  | `██` |
| CS_Debug | Latency (ms) | 50 |0.09 | **0.09** | 0.09 | 0.09 | 🟢 | `█` |
| CS_Debug | Alloc (count) | 5 |107.00 | **107.00** | 107.00 | 107.00 |  | `█` |
| CS_Sequential | Latency (ms) | 50 |2.53 | **2.56** | 2.65 | 2.63 | 🟢 | `██` |
| CS_Sequential | Alloc (count) | 5 |513.05 | **513.05** | 513.05 | 513.05 |  | `█████` |

### Initialization Only

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| Init_Hook | Latency (ms) | 50 |0.05 | **0.05** | 0.06 | 0.06 | 🟢 | `█` |
| Init_Hook | Alloc (count) | 5 |89.00 | **89.00** | 89.01 | 89.01 |  | `█` |
| Init_Root | Latency (ms) | 50 |0.05 | **0.05** | 0.06 | 0.05 | 🟢 | `█` |
| Init_Root | Alloc (count) | 5 |89.00 | **89.00** | 89.00 | 89.00 |  | `█` |
| Init_Tamper | Latency (ms) | 50 |0.05 | **0.06** | 0.06 | 0.06 | 🟢 | `█` |
| Init_Tamper | Alloc (count) | 5 |89.00 | **89.00** | 89.01 | 89.01 |  | `█` |
| Init_Debug | Latency (ms) | 50 |0.06 | **0.06** | 0.06 | 0.06 | 🟢 | `█` |
| Init_Debug | Alloc (count) | 5 |89.00 | **89.00** | 89.00 | 89.00 |  | `█` |
| Init_All | Latency (ms) | 50 |0.11 | **0.12** | 0.12 | 0.12 | 🟢 | `█` |
| Init_All | Alloc (count) | 5 |171.00 | **171.01** | 171.01 | 171.01 |  | `█` |
| Init_Emulator | Latency (ms) | 50 |0.06 | **0.06** | 0.07 | 0.07 | 🟢 | `█` |
| Init_Emulator | Alloc (count) | 5 |89.00 | **89.00** | 89.00 | 89.00 |  | `█` |
| Init_Sequential | Latency (ms) | 50 |0.10 | **0.11** | 0.11 | 0.11 | 🟢 | `█` |
| Init_Sequential | Alloc (count) | 5 |129.00 | **129.01** | 129.02 | 129.02 |  | `█` |
| Init_Location | Latency (ms) | 50 |0.05 | **0.06** | 0.06 | 0.06 | 🟢 | `█` |
| Init_Location | Alloc (count) | 5 |71.00 | **71.00** | 71.00 | 71.00 |  | `█` |

### Memory Allocation Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| Mem_AllocationMultiple | Latency (ms) | 50 |13.17 | **13.43** | 19.38 | 14.50 | 🟢 | `████` |
| Mem_AllocationMultiple | Alloc (count) | 5 |3691.29 | **3691.29** | 3692.29 | 3692.29 |  | `██████████` |
| Mem_AllocationSingle | Latency (ms) | 50 |2.69 | **2.78** | 4.09 | 3.20 | 🟢 | `██` |
| Mem_AllocationSingle | Alloc (count) | 5 |744.06 | **744.06** | 744.06 | 744.06 |  | `███████` |
| Mem_PressureHeavy | Latency (ms) | 50 |5.57 | **5.82** | 7.75 | 6.14 | 🟢 | `███` |
| Mem_PressureHeavy | Alloc (count) | 5 |1879.12 | **1879.12** | 1879.47 | 1879.47 |  | `██████████` |

### Instance Reuse Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| ReuseSingle | Latency (ms) | 50 |2.85 | **2.89** | 3.99 | 3.22 | 🟢 | `██` |
| ReuseSingle | Alloc (count) | 5 |572.06 | **572.06** | 572.06 | 572.06 |  | `█████` |
| ReuseInstance | Latency (ms) | 50 |40.48 | **41.06** | 68.36 | 42.17 | 🟡 | `██████` |
| ReuseInstance | Alloc (count) | 5 |5649.00 | **5649.00** | 5649.00 | 5649.00 |  | `██████████` |

### Edge Cases

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| AllModulesConcurrent | Latency (ms) | 50 |8.83 | **9.49** | 12.90 | 9.86 | 🟢 | `███` |
| AllModulesConcurrent | Alloc (count) | 5 |1700.20 | **1700.20** | 1701.40 | 1701.40 |  | `██████████` |
| EmptyConfiguration | Latency (ms) | 50 |0.06 | **0.06** | 0.07 | 0.07 | 🟢 | `█` |
| EmptyConfiguration | Alloc (count) | 5 |83.00 | **83.00** | 83.00 | 83.00 |  | `█` |

### Manual Warmup Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| WithManualWarmup | Latency (ms) | 50 |2.93 | **3.09** | 4.44 | 3.29 | 🟢 | `██` |
| WithManualWarmup | Alloc (count) | 5 |572.06 | **572.06** | 572.25 | 572.25 |  | `█████` |

## Statistical Analysis

| Metric | Value |
| :--- | :--- |
| **Total Tests** | 32 |
| **Average Latency** | 3.02 ms |
| **Median (P50)** | 0.26 ms |
| **P95 Latency** | 13.43 ms |
| **P99 Latency** | 41.06 ms |
| **Fastest Test** | 0.01 ms |
| **Slowest Test** | 41.06 ms |
| **Std Deviation** | 7.42 ms |
| **Coeff. of Variation** | 245.77% |

### Performance Distribution

| Status | Count | Percentage |
| :--- | :---: | :---: |
| 🟢 Excellent (< 20ms) | 31 | 96.9% |
| 🟡 Good (20-50ms) | 1 | 3.1% |
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

