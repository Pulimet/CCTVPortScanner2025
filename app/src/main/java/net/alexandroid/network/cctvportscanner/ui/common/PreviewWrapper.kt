package net.alexandroid.network.cctvportscanner.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import net.alexandroid.network.cctvportscanner.di.Koin.appModule
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    val appContext = LocalContext.current.applicationContext
    KoinApplicationPreview(
        application = {
            modules(appModule)
            androidContext(appContext)
        }
    ) {
        MyTheme {
            content()
        }
    }
}