# Benchmark Performance Report

> **Generated on:** 2026-05-09 15:59:12

> **Version:** v1.8.4.beta

## Device Information

| Property | Value |
| :--- | :--- |
| **Device** | HONOR STK-LX1 |
| **Android** | API 29 |
| **CPU** | 8 cores @ 2189 MHz |
| **Memory** | 3.6 GB RAM |

---

### Runtime Performance (Inspect Only)

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| SequentialModules | Latency (ms) | 50 |9.48 | **10.20** | 10.80 | 10.72 | 🟢 | `████` |
| SequentialModules | Alloc (count) | 5 |400.22 | **400.22** | 400.22 | 400.22 |  | `████` |
| EmulatorOnly | Latency (ms) | 50 |0.18 | **0.18** | 0.19 | 0.19 | 🟢 | `█` |
| EmulatorOnly | Alloc (count) | 5 |16.00 | **16.00** | 16.01 | 16.01 |  | `█` |
| LocationOnly | Latency (ms) | 50 |8.58 | **20.92** | 29.44 | 23.52 | 🟡 | `██████` |
| LocationOnly | Alloc (count) | 5 |132.40 | **132.40** | 132.40 | 132.40 |  | `█` |
| AllModules | Latency (ms) | 50 |16.13 | **17.26** | 20.71 | 19.67 | 🟢 | `████` |
| AllModules | Alloc (count) | 5 |519.33 | **519.33** | 519.83 | 519.83 |  | `█████` |
| HookOnly | Latency (ms) | 50 |7.38 | **7.54** | 7.73 | 7.69 | 🟢 | `███` |
| HookOnly | Alloc (count) | 5 |311.15 | **311.15** | 311.15 | 311.15 |  | `███` |
| DebugOnly | Latency (ms) | 50 |0.04 | **0.04** | 0.04 | 0.04 | 🟢 | `█` |
| DebugOnly | Alloc (count) | 5 |19.00 | **19.00** | 19.00 | 19.00 |  | `█` |
| RootOnly | Latency (ms) | 50 |1.60 | **1.82** | 2.36 | 2.31 | 🟢 | `██` |
| RootOnly | Alloc (count) | 5 |51.04 | **51.04** | 51.10 | 51.10 |  | `█` |
| TamperOnly | Latency (ms) | 50 |0.33 | **0.37** | 0.61 | 0.52 | 🟢 | `█` |
| TamperOnly | Alloc (count) | 5 |70.01 | **70.01** | 70.02 | 70.02 |  | `█` |

### Cold Start Performance

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| CS_Hook | Latency (ms) | 50 |8.24 | **8.52** | 8.90 | 8.60 | 🟢 | `███` |
| CS_Hook | Alloc (count) | 5 |436.18 | **436.18** | 437.55 | 437.55 |  | `████` |
| CS_Root | Latency (ms) | 50 |2.22 | **2.48** | 3.05 | 3.02 | 🟢 | `██` |
| CS_Root | Alloc (count) | 5 |176.05 | **176.05** | 176.14 | 176.14 |  | `█` |
| CS_All | Latency (ms) | 50 |17.43 | **18.51** | 23.64 | 21.97 | 🟢 | `████` |
| CS_All | Alloc (count) | 5 |762.40 | **762.40** | 762.60 | 762.60 |  | `███████` |
| CS_Tamper | Latency (ms) | 50 |1.09 | **1.17** | 1.35 | 1.29 | 🟢 | `██` |
| CS_Tamper | Alloc (count) | 5 |195.02 | **195.02** | 195.21 | 195.21 |  | `█` |
| CS_Emulator | Latency (ms) | 50 |0.71 | **0.73** | 0.77 | 0.76 | 🟢 | `█` |
| CS_Emulator | Alloc (count) | 5 |141.01 | **141.01** | 141.07 | 141.07 |  | `█` |
| CS_Location | Latency (ms) | 50 |6.77 | **22.83** | 29.29 | 26.27 | 🟡 | `██████` |
| CS_Location | Alloc (count) | 5 |230.50 | **230.50** | 230.50 | 230.50 |  | `██` |
| CS_Debug | Latency (ms) | 50 |0.52 | **0.55** | 0.56 | 0.56 | 🟢 | `█` |
| CS_Debug | Alloc (count) | 5 |144.01 | **144.03** | 144.03 | 144.03 |  | `█` |
| CS_Sequential | Latency (ms) | 50 |10.40 | **10.56** | 11.45 | 11.28 | 🟢 | `████` |
| CS_Sequential | Alloc (count) | 5 |583.22 | **583.22** | 583.56 | 583.56 |  | `█████` |

### Initialization Only

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| Init_Hook | Latency (ms) | 50 |0.43 | **0.44** | 0.46 | 0.45 | 🟢 | `█` |
| Init_Hook | Alloc (count) | 5 |125.01 | **125.01** | 125.02 | 125.02 |  | `█` |
| Init_Root | Latency (ms) | 50 |0.42 | **0.44** | 0.49 | 0.45 | 🟢 | `█` |
| Init_Root | Alloc (count) | 5 |125.01 | **125.02** | 125.11 | 125.11 |  | `█` |
| Init_Tamper | Latency (ms) | 50 |0.42 | **0.43** | 0.45 | 0.44 | 🟢 | `█` |
| Init_Tamper | Alloc (count) | 5 |125.01 | **125.02** | 125.03 | 125.03 |  | `█` |
| Init_Debug | Latency (ms) | 50 |0.42 | **0.43** | 0.44 | 0.44 | 🟢 | `█` |
| Init_Debug | Alloc (count) | 5 |125.01 | **125.02** | 125.07 | 125.07 |  | `█` |
| Init_All | Latency (ms) | 50 |0.83 | **0.87** | 0.89 | 0.88 | 🟢 | `█` |
| Init_All | Alloc (count) | 5 |243.02 | **243.03** | 243.04 | 243.04 |  | `██` |
| Init_Emulator | Latency (ms) | 50 |0.42 | **0.43** | 0.44 | 0.44 | 🟢 | `█` |
| Init_Emulator | Alloc (count) | 5 |125.01 | **125.01** | 125.02 | 125.02 |  | `█` |
| Init_Sequential | Latency (ms) | 50 |0.62 | **0.65** | 0.67 | 0.66 | 🟢 | `█` |
| Init_Sequential | Alloc (count) | 5 |183.01 | **183.02** | 183.03 | 183.03 |  | `█` |
| Init_Location | Latency (ms) | 50 |0.31 | **0.33** | 0.36 | 0.34 | 🟢 | `█` |
| Init_Location | Alloc (count) | 5 |98.01 | **98.02** | 98.02 | 98.02 |  | `█` |

### Memory Allocation Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| Mem_AllocationMultiple | Latency (ms) | 50 |82.04 | **91.67** | 158.21 | 140.94 | 🟠 | `████████` |
| Mem_AllocationMultiple | Alloc (count) | 5 |3783.00 | **3783.00** | 3789.00 | 3789.00 |  | `██████████` |
| Mem_AllocationSingle | Latency (ms) | 50 |16.59 | **17.84** | 20.99 | 20.00 | 🟢 | `████` |
| Mem_AllocationSingle | Alloc (count) | 5 |762.40 | **762.40** | 763.20 | 763.20 |  | `███████` |
| Mem_PressureHeavy | Latency (ms) | 50 |30.84 | **31.99** | 36.44 | 36.25 | 🟡 | `██████` |
| Mem_PressureHeavy | Alloc (count) | 5 |2201.67 | **2201.67** | 2202.67 | 2202.67 |  | `██████████` |

### Instance Reuse Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| ReuseSingle | Latency (ms) | 50 |16.09 | **17.04** | 19.61 | 19.14 | 🟢 | `████` |
| ReuseSingle | Alloc (count) | 5 |519.40 | **519.40** | 520.00 | 520.00 |  | `█████` |
| ReuseInstance | Latency (ms) | 50 |160.35 | **167.76** | 194.51 | 185.71 | 🔴 | `██████████` |
| ReuseInstance | Alloc (count) | 5 |5120.00 | **5120.00** | 5123.00 | 5123.00 |  | `██████████` |

### Edge Cases

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| AllModulesConcurrent | Latency (ms) | 50 |47.91 | **50.28** | 60.49 | 55.91 | 🟠 | `████████` |
| AllModulesConcurrent | Alloc (count) | 5 |1543.00 | **1543.00** | 1543.00 | 1543.00 |  | `██████████` |
| EmptyConfiguration | Latency (ms) | 50 |0.35 | **0.37** | 0.38 | 0.37 | 🟢 | `█` |
| EmptyConfiguration | Alloc (count) | 5 |110.01 | **110.02** | 110.06 | 110.06 |  | `█` |

### Manual Warmup Tests

| Test Case | Metric  | Run | Min | Median | Max | P95 | Status | Visual |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| WithManualWarmup | Latency (ms) | 50 |16.07 | **16.83** | 19.14 | 18.96 | 🟢 | `████` |
| WithManualWarmup | Alloc (count) | 5 |519.33 | **519.33** | 519.50 | 519.50 |  | `█████` |

## Statistical Analysis

| Metric | Value |
| :--- | :--- |
| **Total Tests** | 32 |
| **Average Latency** | 16.30 ms |
| **Median (P50)** | 2.48 ms |
| **P95 Latency** | 91.67 ms |
| **P99 Latency** | 167.76 ms |
| **Fastest Test** | 0.04 ms |
| **Slowest Test** | 167.76 ms |
| **Std Deviation** | 32.81 ms |
| **Coeff. of Variation** | 201.35% |

### Performance Distribution

| Status | Count | Percentage |
| :--- | :---: | :---: |
| 🟢 Excellent (< 20ms) | 26 | 81.3% |
| 🟡 Good (20-50ms) | 3 | 9.4% |
| 🟠 Acceptable (50-100ms) | 2 | 6.3% |
| 🔴 Critical (> 100ms) | 1 | 3.1% |

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

