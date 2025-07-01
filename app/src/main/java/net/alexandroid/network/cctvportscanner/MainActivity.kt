package net.alexandroid.network.cctvportscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.alexandroid.network.cctvportscanner.ui.AppWrapper
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        homeViewModel.onCreate()
        enableEdgeToEdge()
        setContent { AppWrapper() }
    }
}