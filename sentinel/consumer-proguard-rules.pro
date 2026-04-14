-keep class sentinel.Sentinel {
    public <init>(...);
    public *** runtime(...);
    public *** inspect(...);
}

-keep class sentinel.core.violation.AndroidViolation$* { *; }

-keep class sentinel.kit.runtime.DebugRuntime {
    void onDebuggerDetected();
}

-keep class sentinel.kit.runtime.HookRuntime {
    void onHookDetected();
}

-keep class sentinel.kit.runtime.RootRuntime {
    void onRootDetected();
}

-keep class kotlin.Metadata { *; }

-keepattributes *Annotation*

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}