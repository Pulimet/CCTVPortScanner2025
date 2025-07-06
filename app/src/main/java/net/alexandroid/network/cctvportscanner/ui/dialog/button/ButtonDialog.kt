package net.alexandroid.network.cctvportscanner.ui.dialog.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.dialog.CustomDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddButtonDialog(buttonDialogViewModel: ButtonDialogViewModel = koinViewModel()) {
    val uiState by buttonDialogViewModel.uiState.collectAsState()

    if (uiState.showAddButtonDialog) {
        CustomDialog(
            title = stringResource(R.string.add_a_new_button),
            onDismissRequest = { buttonDialogViewModel.onDismissAddButtonDialog() },
            height = 320,
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(vertical = 4.dp),
                state = buttonDialogViewModel.dialogTitleState,
                label = { Text(stringResource(R.string.button_title)) },
                lineLimits = TextFieldLineLimits.SingleLine
            )
            OutlinedTextField(
                modifier = Modifier.padding(vertical = 4.dp),
                state = buttonDialogViewModel.dialogPortState,
                label = { Text(stringResource(R.string.enter_port)) },
                lineLimits = TextFieldLineLimits.SingleLine
            )
            BottomRow(
                Modifier.padding(top = 16.dp),
                uiState,
                buttonDialogViewModel
            )
        }
    }
}

@Composable
private fun BottomRow(
    modifier: Modifier = Modifier,
    uiState: ButtonDialogUiState,
    buttonDialogViewModel: ButtonDialogViewModel
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        if (uiState.dialogEditMode) {
            Button(
                onClick = { buttonDialogViewModel.onDeleteButtonClick() },
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(text = stringResource(R.string.delete))
            }
        }

        Button(
            onClick = { buttonDialogViewModel.onAddOrSaveButtonSubmitClick() },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(
                text = stringResource(
                    if (uiState.dialogEditMode) R.string.save else R.string.add
                )
            )
        }
    }
}