package net.alexandroid.network.cctvportscanner.ui.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanStatus

class HomeViewModel(private val portScanRepo: PortScanRepo, private val pingRepo: PingRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val hostNameState = TextFieldState()
    val customPortState = TextFieldState()
    var hostName = ""
    var port = ""

    private var portScanJob: Job? = null

    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

    fun listenForHostNameChange() {
        viewModelScope.launch {
            snapshotFlow { hostNameState.text }.collectLatest { queryText ->
                if (hostName != queryText.toString()) {
                    portScanRepo.validateHost(queryText.toString()) { status ->
                        Log.d("HomeViewModel", "Host validation status: $status")
                        _uiState.value = _uiState.value.copy(
                            hostValidStatus = status, recentPingStatus = Status.UNKNOWN
                        )
                    }
                    hostName = queryText.toString()
                }
            }
        }
    }

    fun listenForPortChange() {
        viewModelScope.launch {
            snapshotFlow { customPortState.text }.collectLatest { queryText ->
                if (port != queryText.toString()) {
                    portScanRepo.validatePort(queryText.toString()) { status, validPorts ->
                        Log.d("HomeViewModel", "Port validation status: $status")
                        _uiState.value = _uiState.value.copy(portValidStatus = status, validPorts = validPorts ?: "")
                    }
                    port = queryText.toString()
                }
            }
        }
    }

    fun onHostPingSubmit() {
        if (_uiState.value.hostValidStatus != Status.SUCCESS && !_uiState.value.isPortScanInProgress) {
            return
        }
        Log.d("HomeViewModel", "onHostPingSubmit")

        _uiState.value = _uiState.value.copy(isPingInProgress = true, recentPingStatus = Status.UNKNOWN)

        viewModelScope.launch {
            val pingResult = pingRepo.pingHost(hostNameState.text.toString())
            Log.d("HomeViewModel", "pingHost complete and pingResult $pingResult")
            _uiState.value = _uiState.value.copy(
                isPingInProgress = false, recentPingStatus = if (pingResult) Status.SUCCESS else Status.FAILURE
            )
        }
    }

    @OptIn(FlowPreview::class)
    fun onPortSubmit() {
        if (_uiState.value.portValidStatus != Status.SUCCESS && !_uiState.value.isPortScanInProgress) {
            return
        }
        Log.d("HomeViewModel", "onPortSubmit")

        _uiState.value = _uiState.value.copy(isPortScanInProgress = true)

        val scanFlow = portScanRepo.scanPorts(
            hostNameState.text.toString(),
            customPortState.text.toString()
        )

        scanFlow
            .onStart { Log.d("HomeViewModel", "Port scan Started") }
            .filter { it.isScanInProgress }
            .sample(1000L) // Emit the most recent "in-progress" item every 1 second
            .onEach { sampledUpdate ->
                _uiState.value = _uiState.value.copy(
                    portScanResults = sampledUpdate.results,
                    isPortScanInProgress = true // It's an in-progress update
                )
            }
            .onCompletion {
                Log.d("HomeViewModel", "Port scan completed")
                _uiState.value = _uiState.value.copy(isPortScanInProgress = false)
            }
            .catch { e -> // Catch errors from the sampling part
                Log.e("HomeViewModel", "Error in sampled progress flow", e)
            }
            .launchIn(viewModelScope) // Launch this part as a separate collector
    }
}