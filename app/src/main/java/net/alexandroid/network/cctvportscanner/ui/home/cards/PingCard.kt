package net.alexandroid.network.cctvportscanner.ui.home.cards

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.common.CustomTextField
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.common.Progress
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import net.alexandroid.network.cctvportscanner.ui.home.PingStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun PingCard(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val isHostNameLongEnough = homeViewModel.hostNameState.text.length > 6

    val borderColor = when (uiState.recentPingStatus) {
        PingStatus.SUCCESS -> Color.Green
        PingStatus.FAILURE -> Color.Red
        else -> Color.Black
    }

    LaunchedEffect(uiState.recentPingStatus) {
        homeViewModel.listenForHostNameChange()
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            CustomTextField(
                textFieldState = homeViewModel.hostNameState,
                enabled = !uiState.isPingInProgress,
                label = "Enter ip/url...",
                placeholder = "192.168.0.1",
                onSubmitted = { homeViewModel.onHostPingSubmit() }
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (uiState.isPingInProgress) {
                    Progress(modifier = Modifier.padding(end = 8.dp))
                } else {
                    FilledTonalButton(
                        onClick = { homeViewModel.onHostPingSubmit() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        enabled = isHostNameLongEnough
                    ) {
                        Text(stringResource(R.string.ping))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PingCardPreview() {
    PreviewWrapper {
        PingCard()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PingCardDarkPreview() {
    PreviewWrapper {
        PingCard()
    }
}