package net.alexandroid.network.cctvportscanner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.alexandroid.network.cctvportscanner.ui.bottom.BottomBar
import net.alexandroid.network.cctvportscanner.ui.home.HomeScreen
import net.alexandroid.network.cctvportscanner.ui.top.TopBar
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@Composable
fun AppWrapper() {
    MyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar() },
            bottomBar = { BottomBar() }
        ) { innerPadding ->
            HomeScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}