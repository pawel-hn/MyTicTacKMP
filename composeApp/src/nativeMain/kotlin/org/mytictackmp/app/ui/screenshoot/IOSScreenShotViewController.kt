package org.mytictackmp.app.ui.screenshoot

import kotlinx.coroutines.flow.Flow

class IOSScreenShotViewController : ScreenShotViewController {
    override suspend fun takeAndShareScreenShot(screenshotFileName: String): TakeAndShareScreenshotResult {
        TODO("Not yet implemented")
    }

    override val events: Flow<ScreenShootEvent>
        get() = TODO("Not yet implemented")
}