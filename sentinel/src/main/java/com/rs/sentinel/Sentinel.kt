package com.rs.sentinel

import android.content.Context
import android.location.Location
import com.rs.kit.debug.detector.DebugDetector
import com.rs.kit.emulator.detector.EmulatorDetector
import com.rs.kit.hook.detector.HookDetector
import com.rs.kit.location.detector.MockLocationAppDetector
import com.rs.kit.location.detector.MockLocationDetector
import com.rs.kit.location.detector.MockLocationSettingDetector
import com.rs.kit.root.detector.RootDetector
import com.rs.kit.tamper.detector.TamperDetector
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.logger.Logger
import com.rs.sentinel.report.SecurityReport

class Sentinel private constructor(
    private val detectors: List<SecurityDetector>,
    private val threshold: Int,
) {
    private val logger by lazy(::Logger)

    fun inspect(): SecurityReport {
        val threads = detectors.flatMap { detector -> detector.detect().orEmpty() }

        return SecurityReport(
            threats = threads,
            threshold = threshold
        )
    }

    fun log(report: SecurityReport, output: (String) -> Unit) {
        logger.log(
            report = report,
            logger = output
        )
    }

    class Builder(
        private val context: Context,
    ) {
        private val detectors = mutableListOf<SecurityDetector>()

        private val config = Config(
            threshold = DEFAULT_THRESHOLD
        )

        fun config(block: Config.() -> Unit) {
            config.apply(block)
        }

        fun root() {
            detectors.add(element = RootDetector(context = context))
        }

        fun tamper() {
            detectors.add(
                element = TamperDetector(
                    context = context,
                    packageName = config.packageName,
                    packageSignature = config.packageSignature
                )
            )
        }

        fun hook() {
            detectors.add(element = HookDetector())
        }

        fun emulator() {
            detectors.add(element = EmulatorDetector())
        }

        fun debug() {
            detectors.add(element = DebugDetector(context = context))
        }

        fun location() {
            detectors.add(element = MockLocationSettingDetector(context = context))
            detectors.add(element = MockLocationAppDetector(context = context))
        }

        fun location(location: Location) {
            detectors.add(element = MockLocationDetector(location = location))
        }

        fun all() {
            root()
            tamper()
            hook()
            emulator()
            debug()
            location()
        }

        fun build() = Sentinel(
            detectors = detectors.toList(),
            threshold = config.threshold
        )
    }

    companion object {

        private const val DEFAULT_THRESHOLD = 80

        inline fun configure(
            context: Context,
            block: Builder.() -> Unit,
        ): Sentinel = Builder(context = context).apply(block).build()
    }
}