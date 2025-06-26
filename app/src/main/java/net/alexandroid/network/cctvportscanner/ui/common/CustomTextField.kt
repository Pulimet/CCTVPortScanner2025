package net.alexandroid.network.cctvportscanner.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomTextField(
    textFieldState: TextFieldState = TextFieldState(),
    enabled: Boolean = true,
    label: String = "",
    placeholder: String = "",
    onSubmitted: () -> Unit = {},
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val isHostNameLongEnough = homeViewModel.hostNameState.text.length > 6

    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Blue, Color.Magenta, Color.Red, Color.Blue, Color.Magenta)
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isPingInProgress,
        state = homeViewModel.hostNameState,
        label = { Text("Enter ip/url...") },
        placeholder = { Text("192.168.0.1", color = Color.Gray) },
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = TextStyle(brush = brush),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = { performDefaultAction ->
            if (isHostNameLongEnough) {
                homeViewModel.onIpSubmit()
                performDefaultAction()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    MyTheme {
        CustomTextField()
    }
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun CustomTextFieldDarkPreview() {
    MyTheme {
        CustomTextField()
    }
}