package org.mytictackmp.app.ui.screenshoot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer

@Composable
fun ScreenShootScreen(
    modifier: Modifier = Modifier,
    controller: ScreenShotViewController,
    contentToScreenShot: @Composable () -> Unit
) {
    val graphicsLayer = rememberGraphicsLayer()

    ScreenShootEventWrapper(graphicsLayer, controller)

    Box(modifier = modifier
        .drawWithContent {
            graphicsLayer.record {
                this@drawWithContent.drawContent()
            }
            drawLayer(graphicsLayer)
        }
    ) {
        contentToScreenShot()
    }
}