package org.mytictackmp.app.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun TicTacBackHandler(onBack: () -> Unit) {
    BackHandler { onBack() }
}