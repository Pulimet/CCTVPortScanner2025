package net.alexandroid.network.cctvportscanner.ui.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo

class HomeViewModel(private val portScanRepo: PortScanRepo, private val pingRepo: PingRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val hostNameState = TextFieldState()
    var hostName = ""

    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

    fun listenForHostNameChange() {
        viewModelScope.launch {
            snapshotFlow { hostNameState.text }
                .collectLatest { queryText ->
                    Log.d("HomeViewModel", "onHostNameChange: $queryText, hostNameState: ${hostNameState.text}")
                    if (uiState.value.recentPingStatus != PingStatus.UNKNOWN && hostName != queryText.toString()) {
                        _uiState.value = _uiState.value.copy(recentPingStatus = PingStatus.UNKNOWN)
                    }
                    hostName = queryText.toString()
                }
        }
    }

    fun onHostPingSubmit() {
        _uiState.value = _uiState.value.copy(isPingInProgress = true, recentPingStatus = PingStatus.UNKNOWN)

        viewModelScope.launch {
            val pingResult = pingRepo.pingHost(hostNameState.text.toString())
            Log.d("HomeViewModel", "pingHost complete and pingResult $pingResult")
            _uiState.value = _uiState.value.copy(
                isPingInProgress = false,
                recentPingStatus = if (pingResult) PingStatus.SUCCESS else PingStatus.FAILURE
            )
        }
    }

}