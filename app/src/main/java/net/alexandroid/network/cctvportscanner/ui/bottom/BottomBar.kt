package net.alexandroid.network.cctvportscanner.ui.bottom

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.dialog.button.ButtonDialogViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(buttonDialogViewModel: ButtonDialogViewModel = koinViewModel()) {
    BottomAppBar(
        actions = {
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable { buttonDialogViewModel.deleteAllButtons() }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_all))
                    Text(text = stringResource(R.string.delete_all).uppercase(), fontSize = 12.sp) // Your visible label
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { buttonDialogViewModel.onAddButtonClick() },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_a_new_button))
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    PreviewWrapper {
        BottomBar()
    }
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun BottomBarDarkPreview() {
    PreviewWrapper {
        BottomBar()
    }
}