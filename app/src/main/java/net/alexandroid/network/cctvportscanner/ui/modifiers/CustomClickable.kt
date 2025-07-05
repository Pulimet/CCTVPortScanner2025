package net.alexandroid.network.cctvportscanner.ui.modifiers

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Modifier.customClickable(
    interactionSource: MutableInteractionSource?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
): Modifier {
    val viewConfiguration = LocalViewConfiguration.current

    LaunchedEffect(interactionSource, onClick, onLongClick) {
        var isLongClick = false

        interactionSource?.interactions?.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onLongClick()
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        onClick()
                    }
                }
            }
        }
    }

    return this
}
