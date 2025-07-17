package net.alexandroid.network.cctvportscanner.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import net.alexandroid.network.cctvportscanner.ui.bottom.BottomBar
import net.alexandroid.network.cctvportscanner.ui.home.HomeScreen
import net.alexandroid.network.cctvportscanner.ui.home.HomeScreenLandscape
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme
import net.alexandroid.network.cctvportscanner.ui.top.TopBar

@Composable
fun AppWrapper() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    MyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar() },
            bottomBar = {
                if (!isLandscape) {
                    BottomBar()
                }
            }
        ) { innerPadding ->
            if (isLandscape) {
                HomeScreenLandscape(modifier = Modifier.padding(innerPadding))
            } else {
                HomeScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}