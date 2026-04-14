package sentinel.monitor

import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIModalPresentationPageSheet
import platform.UIKit.UISheetPresentationControllerDetent
import platform.UIKit.sheetPresentationController

object SentinelMonitor {

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