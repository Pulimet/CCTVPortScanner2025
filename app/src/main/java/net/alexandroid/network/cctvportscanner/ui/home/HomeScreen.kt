package net.alexandroid.network.cctvportscanner.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
        CustomPortCard()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
        ScanResultCard()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
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
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 1.dp)
            CustomPortCard()
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 1.dp)
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PreviewWrapper {
        HomeScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    PreviewWrapper {
        HomeScreen()
    }
}