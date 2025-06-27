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
    val customPortState = TextFieldState()
    var hostName = ""
    var port = ""

    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

    fun listenForHostNameChange() {
        viewModelScope.launch {
            snapshotFlow { hostNameState.text }
                .collectLatest { queryText ->
                    if (hostName != queryText.toString()) {
                        portScanRepo.validateHost(queryText.toString()) { status ->
                            Log.d("HomeViewModel", "Host validation status: $status")
                            _uiState.value = _uiState.value.copy(
                                hostValidStatus = status,
                                recentPingStatus = Status.UNKNOWN
                            )
                        }
                        hostName = queryText.toString()
                    }
                }
        }
    }

    fun listenForPortChange() {
        viewModelScope.launch {
            snapshotFlow { customPortState.text }
                .collectLatest { queryText ->
                    if (port != queryText.toString()) {
                        portScanRepo.validatePort(queryText.toString()) { status ->
                            Log.d("HomeViewModel", "Port validation status: $status")
                            _uiState.value = _uiState.value.copy(portValidStatus = status)
                        }
                        port = queryText.toString()
                    }
                }
        }
    }

    fun onHostPingSubmit() {
        if (_uiState.value.hostValidStatus != Status.SUCCESS) {
            return
        }
        Log.d("HomeViewModel", "onHostPingSubmit")

        _uiState.value = _uiState.value.copy(isPingInProgress = true, recentPingStatus = Status.UNKNOWN)

        viewModelScope.launch {
            val pingResult = pingRepo.pingHost(hostNameState.text.toString())
            Log.d("HomeViewModel", "pingHost complete and pingResult $pingResult")
            _uiState.value = _uiState.value.copy(
                isPingInProgress = false,
                recentPingStatus = if (pingResult) Status.SUCCESS else Status.FAILURE
            )
        }
    }

    fun onPortSubmit() {
        if (_uiState.value.portValidStatus != Status.SUCCESS) {
            return
        }
        Log.d("HomeViewModel", "onPortSubmit")
    }

}