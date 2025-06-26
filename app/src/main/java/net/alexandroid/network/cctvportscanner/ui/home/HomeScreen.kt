package net.alexandroid.network.cctvportscanner.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.alexandroid.network.cctvportscanner.R
import net.alexandroid.network.cctvportscanner.ui.common.Progress
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PingCard()
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)

    }
}

@Composable
private fun PingCard(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            SimpleFilledTextFieldSample()

            if (uiState.isPingInProgress) {
                Progress(modifier = Modifier.padding(end = 8.dp))
            } else {
                FilledTonalButton(
                    onClick = { homeViewModel.onIpSubmit() },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = homeViewModel.hostNameState.text.length > 6
                ) {
                    Text(stringResource(R.string.ping))
                }
            }
        }
    }
}

@Composable
fun SimpleFilledTextFieldSample(homeViewModel: HomeViewModel = koinViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Blue, Color.Magenta, Color.Red, Color.Blue, Color.Magenta)
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(0.7f),
        enabled = !uiState.isPingInProgress,
        state = homeViewModel.hostNameState,
        label = { Text("Enter ip/url...") },
        placeholder = { Text("192.168.0.1", color = Color.Gray) },
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = TextStyle(brush = brush),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = { performDefaultAction ->
            homeViewModel.onIpSubmit()
            performDefaultAction()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyTheme {
        HomeScreen()
    }
}