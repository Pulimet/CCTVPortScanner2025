package net.alexandroid.network.cctvportscanner.ui.home.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.common.CustomPreviews
import net.alexandroid.network.cctvportscanner.ui.common.CustomTextField
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.common.Status
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomPortCard(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    val borderColor = when (uiState.portValidStatus) {
        Status.SUCCESS -> Color.Green
        Status.FAILURE -> Color.Red
        else -> MaterialTheme.colorScheme.outline
    }

    val label = when (uiState.portValidStatus) {
        Status.SUCCESS -> "Valid port (${uiState.validPorts})"
        Status.FAILURE -> "Non valid port"
        else -> stringResource(R.string.enter_port)
    }

    val isHostValid = uiState.hostValidStatus == Status.SUCCESS
    val isPortValid = uiState.portValidStatus == Status.SUCCESS

    val isTextFieldEnabled = isHostValid && !uiState.isPortScanInProgress
    val isCheckBtnEnabled = isPortValid && !uiState.isPortScanInProgress

    LaunchedEffect(uiState.portValidStatus) {
        homeViewModel.listenForPortChange()
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            CustomTextField(
                textFieldState = homeViewModel.customPortState,
                enabled = isTextFieldEnabled,
                label = label,
                placeholder = stringResource(R.string.port_example),
                onSubmitted = { homeViewModel.onPortScanSubmit() })
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { homeViewModel.onPortScanSubmit() },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = isCheckBtnEnabled,
                ) {
                    Text(stringResource(R.string.check))
                }

            }
        }
    }
}

@CustomPreviews
@Composable
fun CustomPortCardPreview() {
    PreviewWrapper {
        CustomPortCard()
    }
}