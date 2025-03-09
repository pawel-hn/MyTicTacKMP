package org.mytictackmp.app.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mytictackmp.app.MainViewModel
import org.mytictackmp.app.start.StartScreenViewModel

actual val platformModule: Module = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::StartScreenViewModel)
}