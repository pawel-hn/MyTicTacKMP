package org.mytictackmp.app.ui.screenshoot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ScreenShootEventWrapper(
    graphicsLayer: GraphicsLayer,
    screenShotViewController: ScreenShotViewController
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        screenShotViewController.events.collect {
            when (it) {
                is ScreenShootViewUIEvents.GetCurrentGraphicsLayerAndContext -> {
                    it.onCurrentGraphicsLayerAndContext(graphicsLayer, context)
                }
            }
        }
    }
}