package sentinel

import android.location.Location
import android.os.Build

fun Location.isMocked(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    isMock
} else {
    @Suppress("DEPRECATION")
    isFromMockProvider
}