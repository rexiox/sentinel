package com.rs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sentinel.Sentinel
import sentinel.ui.screen.main.SentinelMainScreen
import sentinel.ui.theme.SentinelTheme

@Composable
fun App(
    navigationBarModifier: Modifier = Modifier,
    sentinel: Sentinel,
    appId: String = "",
    appSignature: String = "",
    appHash: String = "",
) {
    SentinelTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
        ) {
            SentinelMainScreen(
                navigationBarModifier = navigationBarModifier,
                sentinel = sentinel,
                appId = appId,
                appSignature = appSignature,
                appHash = appHash
            )
        }
    }
}