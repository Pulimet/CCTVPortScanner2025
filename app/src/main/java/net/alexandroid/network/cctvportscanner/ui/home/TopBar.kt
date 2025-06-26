package net.alexandroid.network.cctvportscanner.ui.home

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(R.string.app_name))
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    MyTheme {
        TopBar()
    }
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun TopBarDarkPreview() {
    MyTheme {
        TopBar()
    }
}