package org.mytictackmp.app.game.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mytictackmp.composeapp.generated.resources.Res
import mytictackmp.composeapp.generated.resources.ic_share
import mytictackmp.composeapp.generated.resources.reset
import mytictackmp.composeapp.generated.resources.save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mytictackmp.app.game.GameDialog
import org.mytictackmp.app.game.GameRouter
import org.mytictackmp.app.game.GameUIEvents
import org.mytictackmp.app.game.GameUIState
import org.mytictackmp.app.game.GameViewModel
import org.mytictackmp.app.ui.MyTicTacTheme
import org.mytictackmp.app.ui.Padding
import org.mytictackmp.app.ui.components.TicTacButton
import org.mytictackmp.app.ui.components.TicTacDialog
import org.mytictackmp.app.ui.screenshoot.ScreenShootScreen

@Composable
fun GameScreen(viewModel: GameViewModel, router: GameRouter) {
    val gameDialog = rememberSaveable { mutableStateOf<GameDialog?>(null) }
    val animationEvent = remember { mutableStateOf<AnimationEvent?>(null) }
    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is GameUIEvents.ShowDialog -> {
                    gameDialog.value = it.dialog
                }

                is GameUIEvents.NavigateToMainScreen -> {
                    router.backToMainScreen()
                }

                is GameUIEvents.ComputerMove -> {
                    animationEvent.value = AnimationEvent.AnimateComputerMove(it.fieldId)
                }

                GameUIEvents.ResetGame -> {
                    animationEvent.value = AnimationEvent.ResetAnimations
                }

                is GameUIEvents.VictoryLine -> {
                    animationEvent.value = AnimationEvent.AnimateWinningLine(it.winningFields)
                }

                is GameUIEvents.GameLoaded -> {
                    animationEvent.value = AnimationEvent.GameLoaded(it.fields)
                }

                is GameUIEvents.ShowToast -> {
                    // Toast
                    viewModel.gameSaved()
                }
            }
        }
    }


    Column(
        modifier = Modifier.background(Color.White).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val result = state) {
            GameUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is GameUIState.CurrentCurrentGameUI -> {
                GameCurrentPlayerHeader(
                    modifier = Modifier.fillMaxWidth().padding(top = Padding.medium),
                    state = result
                )

                ScreenShootScreen(
                    modifier = Modifier.weight(1F),
                    controller = viewModel.screenShotViewController
                ) {
                    GameField(
                        modifier = Modifier
                            .background(color = MyTicTacTheme.colours.backgroundScreen),
                        state = result,
                        animationEvent = animationEvent.value,
                        onTap = { id -> viewModel.fieldTapped(id, false) },
                        setDefault = viewModel::setDefault
                    )
                }





                IconButton(
                    onClick = viewModel::onShareClick,
                    modifier = Modifier.padding(Padding.medium)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_share),
                        contentDescription = null,
                        tint = MyTicTacTheme.colours.interactiveTertiaryContent
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Padding.large),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TicTacButton(
                        width = 120.dp,
                        height = 40.dp,
                        textSize = 12.sp,
                        enabledPrimaryColor = MyTicTacTheme.colours.interactiveTertiary,
                        enabledSecondaryColor = MyTicTacTheme.colours.interactiveTertiaryContent,
                        text = stringResource(Res.string.reset),
                        isSelected = true,
                        onClick = viewModel::reset
                    )
                    TicTacButton(
                        width = 120.dp,
                        height = 40.dp,
                        textSize = 12.sp,
                        enabledPrimaryColor = MyTicTacTheme.colours.interactiveSecondary,
                        enabledSecondaryColor = MyTicTacTheme.colours.interactiveSecondaryContent,
                        text = stringResource(Res.string.save),
                        isSelected = true,
                        onClick = viewModel::saveGame
                    )
                }
            }
        }
    }
    val dialogToShow = gameDialog.value
    if (dialogToShow != null) {
        TicTacDialog(
            gameDialog = dialogToShow,
            onConfirm = {
                gameDialog.value = null
                viewModel.dialogConfirmClick(dialogToShow)
            },
            onCancel = {
                gameDialog.value = null
            }
        )
    }
}
