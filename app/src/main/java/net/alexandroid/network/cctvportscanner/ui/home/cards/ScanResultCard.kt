package net.alexandroid.network.cctvportscanner.ui.home.cards

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.common.PreviewWrapper
import net.alexandroid.network.cctvportscanner.ui.common.Progress
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import net.alexandroid.network.cctvportscanner.utils.PortUtils
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanResultCard(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val result = PortUtils.convertResultToMapWithColors(uiState.portScanResults)

    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (uiState.isPortScanInProgress) {
                Progress(
                    Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                )
            }
            if (result.isNotEmpty()) {
                Text(
                    buildAnnotatedString {
                        result.forEach {
                            it
                            withStyle(style = SpanStyle(color = it.value)) {
                                append(it.key)
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (!uiState.isPortScanInProgress && result.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_results),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    PreviewWrapper {
        ScanResultCard()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressDarkPreview() {
    PreviewWrapper {
        ScanResultCard()
    }
}