package com.rs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sentinel.Sentinel
import sentinel.ui.dashboard.SentinelDashboardScreen
import sentinel.ui.theme.SentinelTheme

@Composable
fun App(
    sentinel: Sentinel,
    appId: String = "",
    appSignature: String = "",
) {
    SentinelTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black
        ) {
            SentinelDashboardScreen(
                sentinel = sentinel,
                appId = appId,
                appSignature = appSignature
            )
        }
    }
}