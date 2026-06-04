package com.rs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sentinel.Sentinel
import sentinel.attest.provider.AttestProvider
import sentinel.ui.screen.main.SentinelMainScreen
import sentinel.ui.theme.SentinelTheme

@Composable
fun App(
    navigationBarModifier: Modifier = Modifier,
    sentinel: Sentinel,
    attestProvider: AttestProvider,
    appId: String = "",
    appIntegrity: String = "",
    onMonitorStart: () -> Unit,
) {
    var showSplashScreen by remember { mutableStateOf(true) }

    SentinelTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            floatingActionButtonPosition = FabPosition.Center
        ) {
            if (showSplashScreen) {
                Splash(
                    sentinel = sentinel,
                    attestProvider = attestProvider,
                    onCompleted = {
                        showSplashScreen = false
                    }
                )
            } else {
                SentinelMainScreen(
                    navigationBarModifier = navigationBarModifier,
                    sentinel = sentinel,
                    appId = appId,
                    appIntegrity = appIntegrity,
                    onMonitorStart = onMonitorStart
                )
            }
        }
    }
}