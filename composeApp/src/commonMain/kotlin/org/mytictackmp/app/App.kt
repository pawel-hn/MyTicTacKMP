package org.mytictackmp.app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.mytictackmp.app.start.StartScreen
import org.mytictackmp.app.ui.MyTicTacTheme

@Composable
@Preview
fun App() {
    MyTicTacTheme {
            KoinContext {
                val viewModel: MainViewModel = koinViewModel()
                val splash by viewModel.isSplashVisible.collectAsStateWithLifecycle()

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