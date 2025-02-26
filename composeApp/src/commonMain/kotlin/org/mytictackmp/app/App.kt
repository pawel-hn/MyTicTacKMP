package org.mytictackmp.app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mytictackmp.app.start.StartScreen
import org.mytictackmp.app.ui.MyTicTacTheme

@Composable
@Preview
fun App() {
    Box(modifier = Modifier.fillMaxSize()) {

        var splash by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(1000)
            splash = false
        }
        MyTicTacTheme {
            Crossfade(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MyTicTacTheme.colours.backgroundScreen)
                    .statusBarsPadding()
                    .systemBarsPadding(),
                targetState = splash,
                animationSpec = tween(1000),
                label = ""
            ) { showSplash ->
                if (showSplash) {
                    SplashScreen()
                } else {
                    StartScreen()
                }
            }
        }
    }


}