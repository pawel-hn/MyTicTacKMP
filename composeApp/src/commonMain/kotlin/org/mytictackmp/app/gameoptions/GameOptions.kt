package org.mytictackmp.app.gameoptions

import kotlinx.serialization.Serializable
import org.mytictackmp.app.data.DifficultyLevel
import org.mytictackmp.app.data.FirstPLayer
import org.mytictackmp.app.data.Player

@Serializable
data class GameOptions(
    val singlePlayer: Boolean,
    val firstPlayer: FirstPLayer,
    val difficultyLevel: DifficultyLevel,
    val cross: Player.Cross,
    val circle: Player.Circle
)
