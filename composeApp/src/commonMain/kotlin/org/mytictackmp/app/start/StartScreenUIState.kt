package org.mytictackmp.app.start

import org.mytictackmp.app.data.DifficultyLevel

data class StartScreenUIState(
    val singlePLayer: Boolean,
    val startScreenFirstPlayerUI: StartScreenFirstPlayerUI,
    val difficultyLevel: DifficultyLevel,
    val loadGameButtonEnabled: Boolean
)

val defaultStartScreenUIState =
    StartScreenUIState(
        singlePLayer = true,
        startScreenFirstPlayerUI = StartScreenFirstPlayerUI.Circle,
        difficultyLevel = DifficultyLevel.EASY,
        loadGameButtonEnabled = false
    )

enum class StartScreenFirstPlayerUI(val label: String, short: Char) {
    Cross("Cross", 'X'),
    Circle("Circle", 'O')
}
