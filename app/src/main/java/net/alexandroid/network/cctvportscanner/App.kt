package net.alexandroid.network.cctvportscanner

import android.app.Application
import net.alexandroid.network.cctvportscanner.di.Koin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Koin.init(this)
    }
}