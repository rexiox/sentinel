package com.rs

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import sentinel.Sentinel
import sentinel.all
import sentinel.configure

fun MainViewController() = ComposeUIViewController {

    val sentinel = remember {
        Sentinel.configure {
            config {
                this.threshold = 20
                this.isLoggingEnabled = true
            }

            all()
            // jailbreak()
            // simulator()
        }
    }

    App(
        sentinel = sentinel,
        appId = Sentinel.Identity.appId
    )
}