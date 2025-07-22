package net.alexandroid.network.cctvportscanner.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.ui.common.CustomLandscapePreviews
import net.alexandroid.network.cctvportscanner.ui.common.CustomPreviews
import net.alexandroid.network.cctvportscanner.ui.common.Divider
import net.alexandroid.network.cctvportscanner.ui.common.LandscapeDivider
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.dialog.button.AddButtonDialog
import net.alexandroid.network.cctvportscanner.ui.home.cards.ButtonsCard
import net.alexandroid.network.cctvportscanner.ui.home.cards.CustomPortCard
import net.alexandroid.network.cctvportscanner.ui.home.cards.PingCard
import net.alexandroid.network.cctvportscanner.ui.home.cards.ScanResultCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PingCard()
        Divider()
        CustomPortCard()
        Divider()
        ScanResultCard()
        Divider()
        ButtonsCard()

        AddButtonDialog()
    }
}

@Composable
fun HomeScreenLandscape(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
        ) {
            PingCard()
            LandscapeDivider()
            CustomPortCard()
            LandscapeDivider()
            ScanResultCard()
        }
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
        ) {
            ButtonsCard()
        }
    }

    AddButtonDialog()
}

@CustomPreviews
@Composable
fun HomeScreenPreview() {
    PreviewWrapper {
        HomeScreen()
    }
}

@CustomLandscapePreviews
@Composable
fun HomeScreenLandscapePreview() {
    PreviewWrapper {
        HomeScreenLandscape()
    }
}