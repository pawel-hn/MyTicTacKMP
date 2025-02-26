package org.mytictackmp.app.start

sealed interface StartScreenUIEvent {
    data object StartGame : StartScreenUIEvent

    data object LoadGame : StartScreenUIEvent
}
