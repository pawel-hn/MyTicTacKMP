package org.mytictackmp.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mytictackmp.app.di.commonModule
import org.mytictackmp.app.di.platformModule

class TicTacApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TicTacApplication)
            modules(commonModule, platformModule)
        }
    }

}