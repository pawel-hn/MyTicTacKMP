package org.mytictackmp.app.start.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mytictackmp.composeapp.generated.resources.Res
import mytictackmp.composeapp.generated.resources.load
import mytictackmp.composeapp.generated.resources.no_of_player
import mytictackmp.composeapp.generated.resources.one_player
import mytictackmp.composeapp.generated.resources.start
import mytictackmp.composeapp.generated.resources.tic_tac_toe
import mytictackmp.composeapp.generated.resources.two_players
import org.jetbrains.compose.resources.stringResource
import org.mytictackmp.app.start.StartRouter
import org.mytictackmp.app.start.StartScreenUIEvent
import org.mytictackmp.app.start.StartScreenViewModel
import org.mytictackmp.app.ui.MyTicTacTheme
import org.mytictackmp.app.ui.Padding
import org.mytictackmp.app.ui.components.TicTacButton

@Composable
fun StartScreen(
    viewModel: StartScreenViewModel,
    router: StartRouter,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startScreenEvent.collect {
            when (it) {
                StartScreenUIEvent.StartGame -> router.onStartGame()
                StartScreenUIEvent.LoadGame -> router.onLoadGame()
            }
        }
    }

    BoxWithConstraints(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Padding.medium)
    ) {
        val height = maxHeight
        val width = maxWidth

        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = stringResource(Res.string.tic_tac_toe),
            fontSize = 24.sp,
            color = MyTicTacTheme.colours.contentPrimary
        )

        Column(
            modifier = Modifier.offset { IntOffset(x = 0, y = (height.toPx() / 4).toInt()) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.no_of_player),
                color = MyTicTacTheme.colours.contentPrimary
            )
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.large),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(2) {
                    TicTacButton(
                        modifier = Modifier.weight(0.5F),
                        text = stringResource(
                            if (it == 0) Res.string.one_player else Res.string.two_players
                        ),
                        width = width * 0.4F,
                        isSelected = state.singlePLayer == (it == 0),
                        onClick = {
                            viewModel.onPlayerCountChanged(it == 0)
                        },
                        enabledPrimaryColor = MyTicTacTheme.colours.interactiveSecondary,
                        enabledSecondaryColor = MyTicTacTheme.colours.interactiveSecondaryContent
                    )
                }
            }

            StartOptions(
                modifier = Modifier.fillMaxWidth(),
                maxWidth = width,
                difficultyLevel = state.difficultyLevel,
                singlePlayer = state.singlePLayer,
                startScreenFirstPlayerUI = state.startScreenFirstPlayerUI,
                onDifficultyChanged = viewModel::onDifficultyChanged,
                onPlayerChanged = viewModel::onFirstPlayerChanged
            )
        }

        Column(
            modifier =
            Modifier.align(Alignment.BottomCenter)
                .padding(bottom = Padding.extraExtraLarge),
            verticalArrangement = Arrangement.spacedBy(Padding.medium)
        ) {
            TicTacButton(
                width = width / 2F,
                height = 50.dp,
                text = stringResource(Res.string.start),
                onClick = viewModel::onStartGameClick,
                isSelected = true,
                enabledPrimaryColor = MyTicTacTheme.colours.interactivePrimary,
                enabledSecondaryColor = MyTicTacTheme.colours.interactivePrimaryContent
            )

            val loadButtonColor by animateColorAsState(
                targetValue =
                if (state.loadGameButtonEnabled) {
                    MyTicTacTheme.colours.interactiveSecondary
                } else {
                    Color.DarkGray
                },
                animationSpec = tween(300, 500),
                label = ""
            )
            TicTacButton(
                width = width / 2F,
                height = 50.dp,
                text = stringResource(Res.string.load),
                onClick = viewModel::onLoadGameClick,
                isSelected = true,
                enabled = state.loadGameButtonEnabled,
                enabledPrimaryColor = loadButtonColor,
                enabledSecondaryColor = MyTicTacTheme.colours.interactiveSecondaryContent
            )
        }
    }
}
