package org.mytictackmp.app.data

import mytictackmp.composeapp.generated.resources.Res
import mytictackmp.composeapp.generated.resources.difficulty_easy
import mytictackmp.composeapp.generated.resources.difficulty_hard
import mytictackmp.composeapp.generated.resources.difficulty_medium
import org.jetbrains.compose.resources.StringResource


enum class DifficultyLevel(val textId: StringResource) {
    EASY(Res.string.difficulty_easy),
    NORMAL(Res.string.difficulty_medium),
    HARD(Res.string.difficulty_hard)
}
