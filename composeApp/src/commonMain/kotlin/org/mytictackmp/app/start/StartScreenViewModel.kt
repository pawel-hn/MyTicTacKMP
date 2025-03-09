package org.mytictackmp.app.start

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mytictackmp.app.data.DifficultyLevel
import org.mytictackmp.app.data.FirstPLayer
import org.mytictackmp.app.gameoptions.GameOptionsService

class StartScreenViewModel(
    private val gameOptionsService: GameOptionsService,
) : ViewModel(), DefaultLifecycleObserver {

    private val _startScreenEvent = Channel<StartScreenUIEvent>()
    val startScreenEvent: Flow<StartScreenUIEvent> = _startScreenEvent.receiveAsFlow()
    private val loadButtonEnabled = MutableStateFlow(false)

    val state: StateFlow<StartScreenUIState> =
        combine(
            gameOptionsService.gameOptions,
            loadButtonEnabled
        ) { options, loadButton ->
            StartScreenUIState(
                singlePLayer = options.singlePlayer,
                startScreenFirstPlayerUI =
                when (options.firstPlayer) {
                    FirstPLayer.Circle -> StartScreenFirstPlayerUI.Circle
                    FirstPLayer.Cross -> StartScreenFirstPlayerUI.Cross
                },
                difficultyLevel = options.difficultyLevel,
                loadGameButtonEnabled = loadButton
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(500),
            defaultStartScreenUIState
        )

    init {
        //  getSavedGame()
    }

    fun onPlayerCountChanged(isSinglePlayer: Boolean) = gameOptionsService.setSinglePlayer(
        isSinglePlayer
    )

    fun onDifficultyChanged(difficultyLevel: DifficultyLevel) =
        gameOptionsService.onDifficultyChanged(
            difficultyLevel
        )

    fun onFirstPlayerChanged(player: StartScreenFirstPlayerUI) =
        gameOptionsService.onFirstPlayerChanged(
            when (player) {
                StartScreenFirstPlayerUI.Circle -> FirstPLayer.Circle
                StartScreenFirstPlayerUI.Cross -> FirstPLayer.Cross
            }
        )

    fun onStartGameClick() {
        viewModelScope.launch {
            _startScreenEvent.send(StartScreenUIEvent.StartGame)
        }
    }

    fun onLoadGameClick() {
        viewModelScope.launch {
            _startScreenEvent.send(StartScreenUIEvent.LoadGame)
        }
    }


}
