package net.alexandroid.network.cctvportscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.alexandroid.network.cctvportscanner.ui.AppWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent { AppWrapper() }

        // TODO Add Crashlytics

        // TODO Redesign UI (incl. dark mode)
        // 1. Play with colors
        // 2. Reorganize UI elements
        // 3. Add animations

        // TODO Release on Google Play Version 1.21
    }
}