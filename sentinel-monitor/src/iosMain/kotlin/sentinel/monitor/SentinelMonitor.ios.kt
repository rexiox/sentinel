package sentinel.monitor

import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIModalPresentationPageSheet
import platform.UIKit.UISheetPresentationControllerDetent
import platform.UIKit.sheetPresentationController

/**
 * SentinelMonitor serves as the primary entry point for managing application integrity
 * and security monitoring processes.
 *
 * This object provides the interface required to initiate the [SentinelMonitorController]
 * for verifying application security.
 */
object SentinelMonitor {

    /**
     * Initiates the security monitoring process by presenting the [SentinelMonitorController].
     *
     * Retrieves the current root view controller and presents the monitor interface.
     *
     * @param appId A list of bytes representing the unique application identifier.
     * @param appIntegrity Integrity data derived from the `embedded.mobileprovision` file.
     * @param threshold The threshold value defined for security checks.
     * @param isBottomSheet Determines whether to present as a bottom sheet (default) or full-screen.
     */
    fun start(
        appId: List<Byte>,
        appIntegrity: List<Byte>,
        threshold: Int,
        isBottomSheet: Boolean = true,
    ) {
        val monitorController = SentinelMonitorController(
            appId = appId,
            appIntegrity = appIntegrity,
            threshold = threshold
        )
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

        rootViewController?.let { root ->
            if (isBottomSheet) {
                monitorController.modalPresentationStyle = UIModalPresentationPageSheet
                monitorController.sheetPresentationController?.let { sheet ->
                    sheet.detents = listOf(
                        UISheetPresentationControllerDetent.largeDetent(),
                        UISheetPresentationControllerDetent.largeDetent()
                    )
                    sheet.preferredCornerRadius = 8.0
                    sheet.prefersGrabberVisible = true
                    sheet.prefersEdgeAttachedInCompactHeight = true
                }
            } else {
                monitorController.modalPresentationStyle = UIModalPresentationFullScreen
            }

            root.presentViewController(
                viewControllerToPresent = monitorController,
                animated = true,
                completion = null
            )
        }
    }
}