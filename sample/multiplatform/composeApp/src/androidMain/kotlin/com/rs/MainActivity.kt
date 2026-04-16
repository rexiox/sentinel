package com.rs

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import sentinel.Sentinel
import sentinel.all
import sentinel.configure
import sentinel.core.ext.toByteList
import sentinel.monitor.SentinelMonitor

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)


        setContent {
            val context = LocalContext.current

            val sentinel = remember {
                Sentinel.configure(context = context) {
                    config {
                        appId = Sentinel.Identity.appId.toByteList()
                        appIntegrity = Sentinel.Identity.appIntegrity.toByteList()
                        threshold = 20
                        // isLoggingEnabled = true
                    }

                    all()
                    // root()
                    // tamper()
                    // hook()
                    // emulator()
                    // debug()
                    // location()
                }
            }

            App(
                navigationBarModifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
                sentinel = sentinel,
                appId = Sentinel.Identity.appId,
                appIntegrity = Sentinel.Identity.appIntegrity.orEmpty(),
                onMonitorStart = {
                    SentinelMonitor.start(
                        context = context,
                        appId = sentinel.config.appId,
                        appIntegrity = sentinel.config.appIntegrity,
                        threshold = sentinel.config.threshold
                    )
                }
            )
        }
    }
}