package net.alexandroid.network.cctvportscanner.ui.home.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.room.button.ButtonEntity
import net.alexandroid.network.cctvportscanner.ui.common.CustomPreviews
import net.alexandroid.network.cctvportscanner.ui.common.LongPressButon
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.dialog.button.ButtonDialogViewModel
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ButtonsCard(homeViewModel: HomeViewModel = koinViewModel(), buttonDialogViewModel: ButtonDialogViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    val isButtonsEnabled = !uiState.isPortScanInProgress

    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (uiState.allButtons.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_buttons),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        uiState.allButtons,
                        key = { button -> button.id }
                    ) { button ->
                        CustomButton(homeViewModel, buttonDialogViewModel, button, isButtonsEnabled)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomButton(
    homeViewModel: HomeViewModel,
    buttonDialogViewModel: ButtonDialogViewModel,
    button: ButtonEntity,
    isButtonsEnabled: Boolean
) {
    LongPressButon(
        onClick = { homeViewModel.onButtonClick(button) },
        onLongClick = { buttonDialogViewModel.onButtonLongClick(button) },
        enabled = isButtonsEnabled,
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp),
        modifier = Modifier
            .height(50.dp)
            .padding(4.dp)
    ) {
        Text(
            text = button.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(
                platformStyle = PlatformTextStyle( // Import PlatformTextStyle
                    includeFontPadding = false
                )
            )
        )
    }
}

@CustomPreviews
@Composable
fun ButtonsCardPreview() {
    PreviewWrapper {
        ButtonsCard()
    }
}