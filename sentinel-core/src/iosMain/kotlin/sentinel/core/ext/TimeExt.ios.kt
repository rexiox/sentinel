package sentinel.core.ext

import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970

actual fun currentTimeMillis(): Long = (NSDate.date().timeIntervalSince1970 * 1000).toLong()