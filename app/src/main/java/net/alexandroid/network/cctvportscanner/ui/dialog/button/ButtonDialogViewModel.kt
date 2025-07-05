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

class ButtonDialogViewModel(
    private val portScanRepo: PortScanRepo,
    private val dbRepo: DbRepo,
) : ViewModel() {

    companion object {
        const val TAG = "ButtonDialogViewModel"
    }

    private val _uiState = MutableStateFlow(ButtonDialogUiState())
    val uiState: StateFlow<ButtonDialogUiState> = _uiState.asStateFlow()

    private var selectedButtonEntity: ButtonEntity? = null

    val dialogTitleState = TextFieldState()
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
        val title = dialogTitleState.text.toString()
        val ports = dialogPortState.text.toString()
        dialogTitleState.setTextAndPlaceCursorAtEnd("")
        dialogPortState.setTextAndPlaceCursorAtEnd("")
        Log.d(TAG, "onAddOrSaveButtonSubmitClick with title: $title. and ports: $ports")
        if (title.trim().isEmpty() || ports.trim().isEmpty()) {
            return
        }
        portScanRepo.validatePort(ports) { status, validPorts ->
            if (status == Status.SUCCESS) {
                _uiState.value = _uiState.value.copy(showAddButtonDialog = false, dialogEditMode = false)
                viewModelScope.launch {
                    if (selectedButtonEntity == null) {
                        dbRepo.insertButton(title, ports)
                    } else {
                        selectedButtonEntity?.let {
                            dbRepo.updateButton(
                                it.copy(
                                    title = title,
                                    ports = ports
                                )
                            )
                        }
                        selectedButtonEntity = null
                    }
                }
            }
        }
    }

    fun onButtonLongClick(button: ButtonEntity) {
        if (!uiState.value.showAddButtonDialog) {
            Log.d(TAG, "onButtonLongClick with title: ${button.title} and ports: ${button.ports}")
            selectedButtonEntity = button
            _uiState.value = _uiState.value.copy(showAddButtonDialog = true, dialogEditMode = true)
            dialogTitleState.setTextAndPlaceCursorAtEnd(button.title)
            dialogPortState.setTextAndPlaceCursorAtEnd(button.ports)
        }
    }

    fun onDeleteButtonClick() {
        Log.d(TAG, "onDeleteButtonClick with title: ${selectedButtonEntity?.title} and ports: ${selectedButtonEntity?.ports}")
        dialogTitleState.setTextAndPlaceCursorAtEnd("")
        dialogPortState.setTextAndPlaceCursorAtEnd("")
        _uiState.value = _uiState.value.copy(showAddButtonDialog = false, dialogEditMode = false)
        viewModelScope.launch {
            selectedButtonEntity?.let {
                dbRepo.deleteButton(it)
            }
            selectedButtonEntity = null
        }
    }

}