package com.rs

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            // hook()
            // simulator()
            // debug()
        }
    }

    App(
        navigationBarModifier = Modifier.padding(bottom = 24.dp),
        sentinel = sentinel,
        appId = Sentinel.Identity.appId
    )
}