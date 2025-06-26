package net.alexandroid.network.cctvportscanner.ui.common

import android.content.res.Configuration
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@Composable
fun Progress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    MyTheme {
        Progress()
    }
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ProgressDarkPreview() {
    MyTheme {
        Progress()
    }
}