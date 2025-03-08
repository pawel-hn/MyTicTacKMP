package org.mytictackmp.app

import android.app.Application
import org.mytictackmp.app.di.initKoin

class TicTacApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

}