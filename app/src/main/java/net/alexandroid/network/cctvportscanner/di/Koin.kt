package net.alexandroid.network.cctvportscanner.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

object Koin {
    fun init(appContext: Context) {
        startKoin {
            androidLogger()
            androidContext(appContext)
            modules(appModule)
        }
    }

    val appModule = module {

    }
}