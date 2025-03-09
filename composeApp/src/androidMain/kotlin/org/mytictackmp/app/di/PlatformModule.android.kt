package org.mytictackmp.app.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mytictackmp.app.MainViewModel
import org.mytictackmp.app.game.GameViewModel
import org.mytictackmp.app.gameengine.GameEngine
import org.mytictackmp.app.gameengine.GameEngineImpl
import org.mytictackmp.app.start.StartScreenViewModel

actual val platformModule: Module = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::StartScreenViewModel)
    viewModelOf(::GameViewModel)
    factory<GameEngine> { GameEngineImpl(get()) }
}