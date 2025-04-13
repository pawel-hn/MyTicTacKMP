package org.mytictackmp.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mytictackmp.app.data.savegame.IsSavedGameUseCase
import org.mytictackmp.app.data.savegame.IsSavedGameUseCaseImpl
import org.mytictackmp.app.data.savegame.LoadGameUseCase
import org.mytictackmp.app.data.savegame.LoadGameUseCaseImpl
import org.mytictackmp.app.data.savegame.SaveGameUseCase
import org.mytictackmp.app.data.savegame.SaveGameUseCaseImpl
import org.mytictackmp.app.gameoptions.GameOptionsService
import org.mytictackmp.app.gameoptions.GameOptionsServiceImpl


val commonModule = module {
    singleOf(::GameOptionsServiceImpl).bind<GameOptionsService>()
    single<SaveGameUseCase> { SaveGameUseCaseImpl(get()) }
    single<LoadGameUseCase> { LoadGameUseCaseImpl(get()) }
    single<IsSavedGameUseCase> { IsSavedGameUseCaseImpl(get()) }
}