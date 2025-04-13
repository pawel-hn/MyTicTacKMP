package org.mytictackmp.app.di


import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mytictackmp.app.MainViewModel
import org.mytictackmp.app.data.savegame.DataStoreManager
import org.mytictackmp.app.data.savegame.DataStoreManagerImpl
import org.mytictackmp.app.data.savegame.createDataStoreNative
import org.mytictackmp.app.game.GameViewModel
import org.mytictackmp.app.gameengine.GameEngine
import org.mytictackmp.app.gameengine.GameEngineImpl
import org.mytictackmp.app.start.StartScreenViewModel

actual val platformModule: Module = module {
    singleOf(::MainViewModel)
    singleOf(::StartScreenViewModel)
    singleOf(::GameViewModel)
    factory<GameEngine> { GameEngineImpl(get(), get(), get()) }
    single<DataStoreManager> {
        DataStoreManagerImpl(
            dataStore = createDataStoreNative()
        )
    }
}