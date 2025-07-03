package net.alexandroid.network.cctvportscanner.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddButtonDialog(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    val titleState = rememberTextFieldState()
    val portState = rememberTextFieldState()

    if (uiState.showAddButtonDialog) {
        CustomDialog(
            title = stringResource(R.string.add_a_new_button),
            onDismissRequest = { homeViewModel.onDismissAddButtonDialog() },
            height = 280,
        ) {
            OutlinedTextField(
                state = titleState,
                label = { Text(stringResource(R.string.button_title)) }
            )
            OutlinedTextField(
                state = portState,
                label = { Text(stringResource(R.string.enter_port)) }
            )
            Button(
                onClick = { homeViewModel.onAddButtonSubmitClick(titleState.text.toString(), portState.text.toString()) },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = stringResource(R.string.add))
            }
        }
    }
}