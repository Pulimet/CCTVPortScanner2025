package net.alexandroid.network.cctvportscanner.ui.common

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import net.alexandroid.network.cctvportscanner.ui.theme.MyTheme

@Composable
fun CustomTextField(
    textFieldState: TextFieldState = TextFieldState(),
    enabled: Boolean = true,
    label: String = "",
    placeholder: String = "",
    suggestionsList: List<String> = listOf(),
    onSubmitted: () -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    val filteredSuggestions = remember(textFieldState.text.toString()) { // Recalculate when value changes
        val filteredList = suggestionsList.filter { suggestion ->
            suggestion.contains(textFieldState.text.toString(), ignoreCase = true)
        }
        if (filteredList.isNotEmpty()) {
            expanded = true
        }
        Log.d("CustomTextField", "filteredSuggestions, filteredList size ${filteredList.size}")
        filteredList
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        state = textFieldState,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color.Gray) },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = { performDefaultAction ->
            if (enabled) {
                onSubmitted()
                performDefaultAction()
            }
        })

    if (filteredSuggestions.isNotEmpty()) {
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .width(200.dp)
                .heightIn(max = 200.dp),
            properties = PopupProperties(focusable = false),
            onDismissRequest = { expanded = false })
        {
            DropdownMenuContent(filteredSuggestions) { selectedText ->
                expanded = false
                textFieldState.setTextAndPlaceCursorAtEnd(selectedText)
            }
        }
    }
}

@Composable
fun DropdownMenuContent(
    filteredSuggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
) {
    filteredSuggestions.forEachIndexed { index, selectionOption ->
        DropdownMenuItem(
            text = {
                SuggestionItemContent(selectionOption)
            }, onClick = {
                onSuggestionSelected(selectionOption)
            }
        )
    }
}

@Composable
private fun SuggestionItemContent(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = LocalTextStyle.current.copy(
            fontSize = 13.sp, lineHeight = 0.sp, lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    MyTheme {
        CustomTextField()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomTextFieldDarkPreview() {
    MyTheme {
        CustomTextField()
    }
}