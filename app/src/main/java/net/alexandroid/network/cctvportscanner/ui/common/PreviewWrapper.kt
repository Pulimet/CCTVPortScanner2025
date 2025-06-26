package net.alexandroid.network.cctvportscanner.ui.common

import androidx.compose.runtime.Composable
import net.alexandroid.network.cctvportscanner.di.Koin.appModule
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme
import org.koin.compose.KoinApplicationPreview

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    KoinApplicationPreview(application = { modules(appModule) }) {
        MyTheme {
            content()
        }
    }
}