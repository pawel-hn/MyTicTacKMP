package org.mytictackmp.app.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mytictackmp.app.data.GameEndResult
import org.mytictackmp.app.gameengine.GameEngine
import org.mytictackmp.app.gameengine.GameEvent
import org.mytictackmp.app.ui.screenshoot.ScreenShotViewController
import org.mytictackmp.app.ui.screenshoot.createScreenShotViewController
import org.mytictackmp.app.utils.makeLog
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object GameViewModelArguments {
    const val LOAD_GAME = "loadGame"
}

class GameViewModel(
    private val gameEngine: GameEngine,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val loadGame: Boolean = savedStateHandle[GameViewModelArguments.LOAD_GAME] ?: false
    val screenShotViewController: ScreenShotViewController = createScreenShotViewController()

    private val _state = MutableStateFlow<GameUIState>(GameUIState.Loading)
    val state: StateFlow<GameUIState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<GameUIEvents>(replay = 1)
    val event: SharedFlow<GameUIEvents> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            gameEngine.state.collect {
                _state.value =
                    GameUIState.CurrentCurrentGameUI(
                        currentPLayer = it.currentPLayer,
                        cross = it.cross,
                        circle = it.circle
                    )
            }
        }

        viewModelScope.launch {
            gameEngine.gameEvent.collect {
                when (it) {
                    is GameEvent.ComputerMove -> {
                        _event.emit(GameUIEvents.ComputerMove(it.fieldId))
                    }

                    is GameEvent.GameEnd -> {
                        if (it.result != GameEndResult.Draw) {
                            _event.emit(GameUIEvents.VictoryLine(it.winningSet))
                        }
                    }

                    is GameEvent.GameLoaded -> {
                        _event.emit(GameUIEvents.GameLoaded(it.fields))
                    }
                }
            }
        }

        if (loadGame) {
            viewModelScope.launch { gameEngine.loadGame() }
        }
    }

    fun onGestureBack() {
        viewModelScope.launch {
            if (isGameRunning()) {
                _event.emit(GameUIEvents.ShowDialog(GameDialog.CancelGame))
            } else {
                _event.emit(GameUIEvents.NavigateToMainScreen)
            }
        }
    }

    fun dialogConfirmClick(dialog: GameDialog) {
        viewModelScope.launch {
            val event =
                when (dialog) {
                    GameDialog.CancelGame -> GameUIEvents.NavigateToMainScreen
                }
            _event.emit(event)
        }
    }

    fun fieldTapped(id: Int, computerMove: Boolean) {
        gameEngine.onFieldSelected(id, computerMove)
    }

    fun reset() {
        if (_state.value !is GameUIState.CurrentCurrentGameUI) return

        viewModelScope.launch {
            _event.emit(GameUIEvents.ResetGame)
        }
    }

    fun saveGame() {
        if (isGameRunning()) {
            viewModelScope.launch {
                val gameSaved = gameEngine.saveGame()
                if (gameSaved.isSuccess) {
                    _event.emit(GameUIEvents.ShowToast(GameToast.GameSaved))
                } else if (gameSaved.isFailure) {
                    _event.emit(GameUIEvents.ShowToast(GameToast.GameSaveFail))
                }
            }
        }
    }

    fun gameSaved() {
        makeLog("gameSaved")
    }

    fun setDefault() {
        gameEngine.setDefault()
    }

    @OptIn(ExperimentalTime::class)
    fun onShareClick() {
        makeLog("onShareClick")
        viewModelScope.launch {
            val result =
                screenShotViewController
                    .takeAndShareScreenShot(
                        "screen_${Clock.System.now().epochSeconds}"
                    )

            makeLog("onShareClick: $result")
        }
    }

    private fun isGameRunning() = gameEngine.state.value.isGameRunning
}


