package org.mytictackmp.app.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.math.abs

enum class MinimumClickInterval(val value: Long) {
    DefaultInterval(300L)
}

class UIClickHelper(
    private val currentTimeProvider: () -> Long = { Clock.System.now().toEpochMilliseconds() },
    private val minimumClickInterval: MinimumClickInterval = MinimumClickInterval.DefaultInterval
) {
    private var lastEventTimeMs = atomic(0L)
    private val mutex = Mutex()


    suspend fun debounceWithoutDelay(lifecycle: Lifecycle, event: () -> Unit) {
        mutex.withLock {
            val now = currentTimeProvider()
            if (abs(now - lastEventTimeMs.value) >= minimumClickInterval.value &&
                lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            ) {
                event.invoke()
                lastEventTimeMs.value = now
            }
        }
    }
}

@Composable
fun rememberUIClickHelper(
    currentTimeProvider: () -> Long = { Clock.System.now().toEpochMilliseconds() },
    minimumClickInterval: MinimumClickInterval = MinimumClickInterval.DefaultInterval
): UIClickHelper = remember {
    UIClickHelper(
        currentTimeProvider = currentTimeProvider,
        minimumClickInterval = minimumClickInterval
    )
}

fun Modifier.debouncedFieldClick(pointerInputKey: Boolean, onClick: (position: Offset) -> Unit) =
    composed {
        val uiClickHelper = rememberUIClickHelper()
        val lifeCycle = LocalLifecycleOwner.current
        val scope = rememberCoroutineScope()
        this.pointerInput(pointerInputKey) {
            detectTapGestures { position ->
                scope.launch {
                    uiClickHelper.debounceWithoutDelay(
                        lifecycle = lifeCycle.lifecycle,
                        event = { onClick(position) }
                    )
                }
            }
        }
    }
