package org.mytictackmp.app.game

import org.mytictackmp.app.data.Player
import org.mytictackmp.app.data.PlayerState

sealed class GameUIState {
    data object Loading : GameUIState()

    data class CurrentCurrentGameUI(
        val currentPLayer: Player,
        val cross: PlayerState,
        val circle: PlayerState
    ) : GameUIState()
}
