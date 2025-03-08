package org.mytictackmp.app

import androidx.compose.ui.window.ComposeUIViewController
import org.mytictackmp.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }