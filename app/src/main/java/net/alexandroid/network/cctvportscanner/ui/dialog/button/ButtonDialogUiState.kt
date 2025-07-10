package net.alexandroid.network.cctvportscanner.ui.dialog.button

import net.alexandroid.network.cctvportscanner.ui.common.Status

data class ButtonDialogUiState(
    val showAddButtonDialog: Boolean = false,
    val dialogEditMode: Boolean = false,
    val portValidStatus: Status = Status.UNKNOWN,
)
