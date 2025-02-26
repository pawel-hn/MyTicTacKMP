package org.mytictackmp.app.data

data class CurrentGame(
    val currentPLayer: Player,
    val cross: PlayerState,
    val circle: PlayerState,
    val isGameRunning: Boolean
)
