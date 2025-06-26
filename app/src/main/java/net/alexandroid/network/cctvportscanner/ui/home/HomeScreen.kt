package net.alexandroid.network.cctvportscanner.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
private fun PingCard() {
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

            Progress()
            FilledTonalButton(onClick = {}, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(stringResource(R.string.ping))
            }
        }
    }
}

@Composable
fun SimpleFilledTextFieldSample(homeViewModel: HomeViewModel = koinViewModel()) {
    var text by remember { mutableStateOf("Hello") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            homeViewModel.onTextChanged(it)
        },
        label = { Text("Label") }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyTheme {
        HomeScreen()
    }
}