package org.mytictackmp.app.ui.screenshoot

import kotlinx.coroutines.flow.Flow

enum class TakeAndShareScreenshotResult {
    ScreenShotShareSuccess,
    ScreenShotShareFail
}

interface ScreenShotViewController {
    suspend fun takeAndShareScreenShot(screenshotFileName: String): TakeAndShareScreenshotResult
    val events: Flow<ScreenShootEvent>
}

interface ScreenShootEvent

expect fun createScreenShotViewController(): ScreenShotViewController

