package com.rs

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import sentinel.Sentinel
import sentinel.all
import sentinel.configure
import sentinel.core.ext.toByteList
import sentinel.monitor.SentinelMonitor

fun MainViewController() = ComposeUIViewController {

    val sentinel = remember {
        Sentinel.configure {
            config {
                appId = Sentinel.Identity.appId.toByteList()
                appIntegrity = Sentinel.Identity.hash.toByteList()
                threshold = 20
                // isLoggingEnabled = true
            }

            all()
            // jailbreak()
            // tamper()
            // hook()
            // simulator()
            // debug()
        }
    }

    App(
        navigationBarModifier = Modifier.padding(bottom = 24.dp),
        sentinel = sentinel,
        appId = Sentinel.Identity.appId,
        appIntegrity = Sentinel.Identity.hash.orEmpty(),
        onMonitorStart = {
            SentinelMonitor.start(
                appId = sentinel.config.appId,
                appIntegrity = sentinel.config.appIntegrity,
                threshold = sentinel.config.threshold
            )
        }
    )
}