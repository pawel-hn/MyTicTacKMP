package org.mytictackmp.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mytictackmp.app.MainViewModel
import org.mytictackmp.app.gameoptions.GameOptionsService
import org.mytictackmp.app.gameoptions.GameOptionsServiceImpl
import org.mytictackmp.app.start.StartScreenViewModel


val commonModule = module {
    singleOf(::GameOptionsServiceImpl).bind<GameOptionsService>()
    viewModelOf(::MainViewModel)
    viewModelOf(::StartScreenViewModel)
}