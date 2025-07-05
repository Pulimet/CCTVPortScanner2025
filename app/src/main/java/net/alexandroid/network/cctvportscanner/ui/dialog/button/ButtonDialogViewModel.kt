package net.alexandroid.network.cctvportscanner.ui.dialog.button

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.alexandroid.network.cctvportscanner.repo.DbRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo
import net.alexandroid.network.cctvportscanner.room.button.ButtonEntity
import net.alexandroid.network.cctvportscanner.ui.common.Status
import net.alexandroid.network.cctvportscanner.ui.home.HomeUiState
import net.alexandroid.network.cctvportscanner.ui.home.HomeViewModel

class ButtonDialogViewModel(
    private val portScanRepo: PortScanRepo,
    private val dbRepo: DbRepo,
) : ViewModel() {

    companion object {
        const val TAG = "ButtonDialogViewModel"
    }

    private val _uiState = MutableStateFlow(ButtonDialogUiState())
    val uiState: StateFlow<ButtonDialogUiState> = _uiState.asStateFlow()

    val dialogHostNameState = TextFieldState()
    val dialogPortState = TextFieldState()

    fun onAddButtonClick() {
        Log.d(TAG, "onAddButtonClick")
        if (!uiState.value.showAddButtonDialog) {
            _uiState.value = _uiState.value.copy(showAddButtonDialog = true)
        }
    }

    fun onDismissAddButtonDialog() {
        Log.d(TAG, "onDismissAddButtonDialog")
        if (uiState.value.showAddButtonDialog) {
            _uiState.value = _uiState.value.copy(showAddButtonDialog = false)
        }
    }

    fun onAddOrSaveButtonSubmitClick() {
        val title = dialogHostNameState.text.toString()
        val ports = dialogPortState.text.toString()
        Log.d(TAG, "onAddButtonClick with title: $title and ports: $ports")
        if (title.trim().isEmpty() || ports.trim().isEmpty()) {
            return
        }
        portScanRepo.validatePort(ports) { status, validPorts ->
            if (status == Status.SUCCESS) {
                _uiState.value = _uiState.value.copy(showAddButtonDialog = false, dialogEditMode = false)
                viewModelScope.launch {
                    dbRepo.deleteButton(ButtonEntity(title = title, ports = ports))
                    dbRepo.insertButton(title, ports)
                }
            }
        }
    }

    fun onButtonLongClick(button: ButtonEntity) {
        if (!uiState.value.showAddButtonDialog) {
            Log.d(TAG, "onButtonLongClick with title: ${button.title} and ports: ${button.ports}")
            _uiState.value = _uiState.value.copy(showAddButtonDialog = true, dialogEditMode = true)
            dialogHostNameState.setTextAndPlaceCursorAtEnd(button.title)
            dialogPortState.setTextAndPlaceCursorAtEnd(button.ports)
        }
    }

    fun onDeleteButtonClick() {
        val title = dialogHostNameState.text.toString()
        val ports = dialogPortState.text.toString()
        Log.d(TAG, "onDeleteButtonClick with title: $title and ports: $ports")
        viewModelScope.launch {
            dbRepo.deleteButton(ButtonEntity(title = title, ports = ports))
        }
    }

}