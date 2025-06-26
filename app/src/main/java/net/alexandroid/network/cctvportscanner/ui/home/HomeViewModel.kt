package net.alexandroid.network.cctvportscanner.ui.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo

class HomeViewModel(private val portScanRepo: PortScanRepo, private val pingRepo: PingRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val hostNameState = TextFieldState()


    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

    fun onIpSubmit() {
        _uiState.value = _uiState.value.copy(isPingInProgress = true)
        Log.d("HomeViewModel", "onTextChanged called $this")
    }

}