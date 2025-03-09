package org.mytictackmp.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.mytictackmp.app.start.StartScreenViewModel
import org.mytictackmp.app.start.compose.StartScreen


enum class Screen {
    START,
    GAME
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
            )
        }

    }
}
