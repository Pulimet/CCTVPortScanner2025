package net.alexandroid.network.cctvportscanner.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Hello there!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyTheme {
        HomeScreen()
    }
}