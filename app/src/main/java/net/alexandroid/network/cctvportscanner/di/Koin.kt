package net.alexandroid.network.cctvportscanner.di

import android.content.Context
import androidx.room.Room
import net.alexandroid.network.cctvportscanner.repo.DataStoreRepo
import net.alexandroid.network.cctvportscanner.repo.DbRepo
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo
import net.alexandroid.network.cctvportscanner.room.ScannerDatabase
import net.alexandroid.network.cctvportscanner.ui.dialog.button.ButtonDialogViewModel
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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
        singleOf(::PortScanRepo)
        singleOf(::PingRepo)
        singleOf(::DbRepo)
        singleOf(::DataStoreRepo)

        viewModelOf(::HomeViewModel)
        viewModelOf(::ButtonDialogViewModel)

        single {
            Room.databaseBuilder(androidContext(), ScannerDatabase::class.java, "scanner_database").build()
        }
        single { get<ScannerDatabase>().hostDao() }
        single { get<ScannerDatabase>().buttonDao() }
    }
}