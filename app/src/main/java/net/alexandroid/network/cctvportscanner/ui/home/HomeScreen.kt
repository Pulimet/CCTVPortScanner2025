package net.alexandroid.network.cctvportscanner.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.ui.home.cards.PingCard
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PingCard()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyTheme {
        HomeScreen()
    }
}