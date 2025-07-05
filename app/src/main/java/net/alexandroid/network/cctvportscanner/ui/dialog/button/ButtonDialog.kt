package net.alexandroid.network.cctvportscanner.ui.dialog.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.dialog.CustomDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddButtonDialog(homeViewModel: ButtonDialogViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    if (uiState.showAddButtonDialog) {
        CustomDialog(
            title = stringResource(R.string.add_a_new_button),
            onDismissRequest = { homeViewModel.onDismissAddButtonDialog() },
            height = 280,
        ) {
            OutlinedTextField(
                state = homeViewModel.dialogHostNameState,
                label = { Text(stringResource(R.string.button_title)) }
            )
            OutlinedTextField(
                state = homeViewModel.dialogPortState,
                label = { Text(stringResource(R.string.enter_port)) }
            )
            Row {
                if (uiState.dialogEditMode) {
                    Button(
                        onClick = { homeViewModel.onDeleteButtonClick() },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                }

                Button(
                    onClick = { homeViewModel.onAddOrSaveButtonSubmitClick() },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = stringResource(
                            if (uiState.dialogEditMode) R.string.save else R.string.add
                        )
                    )
                }
            }
        }
    }
}