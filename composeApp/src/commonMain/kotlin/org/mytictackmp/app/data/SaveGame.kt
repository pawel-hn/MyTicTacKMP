package org.mytictackmp.app.data

import kotlinx.serialization.Serializable
import org.mytictackmp.app.gameoptions.GameOptions

@Serializable
data class SaveGame(
    val currentGame: CurrentGame,
    val options: GameOptions
    )