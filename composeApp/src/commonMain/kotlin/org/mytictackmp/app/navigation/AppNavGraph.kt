package org.mytictackmp.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import org.mytictackmp.app.game.GameRouter
import org.mytictackmp.app.game.GameViewModel
import org.mytictackmp.app.game.GameViewModelArguments
import org.mytictackmp.app.game.compose.GameScreen
import org.mytictackmp.app.start.StartRouter
import org.mytictackmp.app.start.StartScreenViewModel
import org.mytictackmp.app.start.compose.StartScreen


enum class Screen {
    START,
    GAME
}

object NavArguments {
    const val LOAD_GAME = GameViewModelArguments.LOAD_GAME
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.START.name
    ) {
        composable(route = Screen.START.name) {
            val viewModel: StartScreenViewModel = koinViewModel()
            StartScreen(
                viewModel = viewModel,
                router = object : StartRouter {
                    override fun onStartGame() {
                        navController.navigate(Screen.GAME.name)
                    }

                    override fun onLoadGame() {
                        navController.navigate(
                            Screen.GAME.name +
                                    "?${NavArguments.LOAD_GAME}=true"
                        )
                    }
                }
            )
        }

        composable(
            route = Screen.GAME.name + "?${NavArguments.LOAD_GAME}={${NavArguments.LOAD_GAME}}",
            arguments =
            listOf(
                navArgument(name = NavArguments.LOAD_GAME) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val viewModel: GameViewModel = koinViewModel()
            GameScreen(
                viewModel = viewModel,
                router =
                object : GameRouter {
                    override fun backToMainScreen() {
                        navController.navigateUp()
                    }
                }
            )
        }

    }
}
