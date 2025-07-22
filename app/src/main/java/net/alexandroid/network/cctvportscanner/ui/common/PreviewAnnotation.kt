package net.alexandroid.network.cctvportscanner.ui.common

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "default",
    group = "portrait",
    showBackground = true
)
@Preview(
    name = "dark mode",
    group = "portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "small font",
    group = "portrait",
    fontScale = 0.5f
)
@Preview(
    name = "large font",
    group = "portrait",
    fontScale = 1.5f
)
annotation class CustomPreviews

@Preview(
    name = "default",
    group = "landscape",
    showBackground = true,
    heightDp = 360,
    widthDp = 800
)
@Preview(
    name = "dark mode",
    group = "landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 360,
    widthDp = 800
)

@Preview(
    name = "small font",
    group = "landscape",
    fontScale = 0.5f,
    heightDp = 360,
    widthDp = 800
)
@Preview(
    name = "large font",
    group = "landscape",
    fontScale = 1.5f,
    heightDp = 360,
    widthDp = 800
)
annotation class CustomLandscapePreviews