package org.mytictackmp.app.ui.screenshoot

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.mytictackmp.app.utils.makeLog
import java.io.File
import kotlin.coroutines.resume

class AndroidScreenShotViewController : ScreenShotViewController {
    private val _events =
        Channel<ScreenShootViewUIEvents>(
            capacity = Channel.UNLIMITED
        )
    override val events: Flow<ScreenShootViewUIEvents> = _events.receiveAsFlow()

    init {
        makeLog("AndroidScreenShotViewController init")
    }

    override suspend fun takeAndShareScreenShot(screenshotFileName: String): TakeAndShareScreenshotResult {
        makeLog("takeAndShareScreenShot")
        val graphicsLayerAndContext = getGraphicsLayerAndContext()
        return takeAndShareScreenshot(
            graphicsLayer = graphicsLayerAndContext.first,
            context = graphicsLayerAndContext.second,
            screenshotFileName = screenshotFileName
        )
    }

    private suspend fun getGraphicsLayerAndContext(): Pair<GraphicsLayer?, Context> {
        val graphicsLayerAndContext =
            suspendCancellableCoroutine<Pair<GraphicsLayer?, Context>> { continuation ->
                _events.trySend(
                    ScreenShootViewUIEvents
                        .GetCurrentGraphicsLayerAndContext { graphicsLayer, context ->
                            continuation.resume(graphicsLayer to context)
                        }
                )
            }
        makeLog("GraphicsLayer and Context received $graphicsLayerAndContext")
        return graphicsLayerAndContext
    }

    private suspend fun takeAndShareScreenshot(
        graphicsLayer: GraphicsLayer?,
        context: Context,
        screenshotFileName: String
    ): TakeAndShareScreenshotResult {
        return if (graphicsLayer == null) {
            makeLog("GraphicsLayer is null")
            return TakeAndShareScreenshotResult.ScreenShotShareFail
        } else {
            try {
                val bitmap = graphicsLayer.toImageBitmap()
                val uri =
                    bitmap.asAndroidBitmap().saveToCacheAndGetUri(
                        context,
                        screenshotFileName
                    )
                if (uri == null) {
                    TakeAndShareScreenshotResult.ScreenShotShareFail
                } else {
                    shareBitmap(context, uri)
                    TakeAndShareScreenshotResult.ScreenShotShareSuccess
                }
            } catch (exception: Throwable) {
                TakeAndShareScreenshotResult.ScreenShotShareFail
            }
        }
    }

    private suspend fun Bitmap.saveToCacheAndGetUri(
        context: Context,
        screenshotFileName: String
    ): Uri? = withContext(
        Dispatchers.IO
    ) {
        return@withContext try {
            val dir = File(context.cacheDir, "screenshots").apply { mkdirs() }
            val file = File(dir, "$screenshotFileName.png")

            file.outputStream().use { out ->
                this@saveToCacheAndGetUri.compress(Bitmap.CompressFormat.PNG, 50, out)
            }

            makeLog("Screenshot saved to cache")
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (exception: Throwable) {
            exception.printStackTrace()
            makeLog("Failed to save screenshot to cache $exception")
            null
        }
    }

    private fun shareBitmap(context: Context, uri: Uri) {
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

        makeLog("Sharing screenshot")
        startActivities(context, arrayOf(intent))
    }
}

sealed interface ScreenShootViewUIEvents : ScreenShootEvent {
    data class GetCurrentGraphicsLayerAndContext(
        val onCurrentGraphicsLayerAndContext: (GraphicsLayer?, Context) -> Unit
    ) : ScreenShootViewUIEvents
}

@Composable
fun rememberScreenShootController(): AndroidScreenShotViewController =
    remember { AndroidScreenShotViewController() }