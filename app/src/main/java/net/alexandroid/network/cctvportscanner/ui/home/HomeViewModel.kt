package net.alexandroid.network.cctvportscanner.ui.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import net.alexandroid.network.cctvportscanner.repo.DataStoreRepo
import net.alexandroid.network.cctvportscanner.repo.DbRepo
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo
import net.alexandroid.network.cctvportscanner.room.button.ButtonEntity
import net.alexandroid.network.cctvportscanner.ui.common.Status

class HomeViewModel(
    private val portScanRepo: PortScanRepo,
    private val pingRepo: PingRepo,
    private val dbRepo: DbRepo,
    private val dataStoreRepo: DataStoreRepo
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val hostNameState = TextFieldState()
    val customPortState = TextFieldState()
    private var hostName = ""
    private var port = ""

    init {
        viewModelScope.launch {
            dbRepo.getAllHostsFlow().collect { hosts ->
                Log.d(TAG, "Hosts from DB: ${hosts.size}")
                val hostList = hosts.map { it.hostName }
                _uiState.value = _uiState.value.copy(allHosts = hostList)
            }
        }
        viewModelScope.launch {
            dbRepo.getAllButtonsFlow().collect { buttons ->
                Log.d(TAG, "Buttons from DB: ${buttons.size}")
                _uiState.value = _uiState.value.copy(allButtons = buttons)
            }
        }

        viewModelScope.launch {
            dataStoreRepo.getRecentHostAndPort({ recentHostAndPort ->
                Log.d(TAG, "Recent host and port from DataStore: $recentHostAndPort")
                recentHostAndPort.let {
                    hostNameState.setTextAndPlaceCursorAtEnd(it.first)
                    customPortState.setTextAndPlaceCursorAtEnd(it.second)
                    onHostNameChanged(it.first)
                    onCustomPortChanged(it.second)
                }
            })
        }
        viewModelScope.launch {
            dataStoreRepo.isDefaultButtonsLoaded { isLoaded ->
                if (isLoaded) return@isDefaultButtonsLoaded
                viewModelScope.launch {
                    dbRepo.loadDefaultButtons()
                    dataStoreRepo.setDefaultDataLoaded()
                }
            }
        }
    }

    // Hosts
    fun listenForHostNameChange() {
        viewModelScope.launch {
            snapshotFlow { hostNameState.text }.collectLatest { queryText ->
                if (hostName != queryText.toString()) {
                    onHostNameChanged(queryText.toString())
                }
            }
        }
    }

    private fun onHostNameChanged(queryText: String) {
        portScanRepo.validateHost(queryText) { status ->
            _uiState.value = _uiState.value.copy(
                hostValidStatus = status, recentPingStatus = Status.UNKNOWN
            )
        }
        hostName = queryText
    }

    fun onHostPingSubmit() {
        if (_uiState.value.hostValidStatus != Status.SUCCESS && !_uiState.value.isPortScanInProgress) {
            return
        }
        Log.d(TAG, "onHostPingSubmit")

        _uiState.value = _uiState.value.copy(isPingInProgress = true, recentPingStatus = Status.UNKNOWN)

        viewModelScope.launch {
            val pingResult = pingRepo.pingHost(hostNameState.text.toString())
            Log.d(TAG, "pingHost complete and pingResult $pingResult")
            _uiState.value = _uiState.value.copy(
                isPingInProgress = false, recentPingStatus = if (pingResult) Status.SUCCESS else Status.FAILURE
            )

            dbRepo.insertHost(hostNameState.text.toString())
            dataStoreRepo.saveRecentHost(hostNameState.text.toString())
        }
    }

    fun onDeleteHost(host: String) {
        Log.d(TAG, "onDeleteHost called with host: $host")
        viewModelScope.launch {
            dbRepo.deleteHost(host)
        }
    }

    // Ports
    fun listenForPortChange() {
        viewModelScope.launch {
            snapshotFlow { customPortState.text }.collectLatest { queryText ->
                if (port != queryText.toString()) {
                    onCustomPortChanged(queryText.toString())
                }
            }
        }
    }

    private fun onCustomPortChanged(queryText: String) {
        portScanRepo.validatePort(queryText) { status, validPorts ->
            _uiState.value = _uiState.value.copy(portValidStatus = status, validPorts = validPorts ?: "")
        }
        port = queryText
    }

    @OptIn(FlowPreview::class)
    fun onPortScanSubmit() {
        if (_uiState.value.portValidStatus != Status.SUCCESS || _uiState.value.isPortScanInProgress) {
            return
        }
        Log.d(TAG, "onPortSubmit")

        _uiState.value = _uiState.value.copy(isPortScanInProgress = true)

        viewModelScope.launch {
            dbRepo.insertHost(hostNameState.text.toString())
            dataStoreRepo.saveRecentHost(hostNameState.text.toString())
            dataStoreRepo.saveRecentPort(customPortState.text.toString())
        }

        scanPorts(customPortState.text.toString())
    }

    // Scan ports
    private fun scanPorts(ports: String) {
        val scanFlow = portScanRepo.scanPorts(
            hostNameState.text.toString(), ports
        )

        scanFlow.onStart { Log.d(TAG, "Port scan Started") }.conflate().onEach { sampledUpdate ->
            Log.d(TAG, "onEach results size: ${sampledUpdate.results.size}")
            _uiState.value = _uiState.value.copy(
                portScanResults = sampledUpdate.results, isPortScanInProgress = true
            )
            delay(500L)
        }.onCompletion {
            Log.d(TAG, "Port scan completed")
            _uiState.value = _uiState.value.copy(isPortScanInProgress = false)
        }.catch { e -> // Catch errors from the sampling part
            Log.e(TAG, "Error in sampled progress flow", e)
        }.launchIn(viewModelScope) // Launch this part as a separate collector
    }

    // Buttons
    fun onButtonClick(button: ButtonEntity) {
        Log.d(TAG, "Button clicked: ${button.title} with ports: ${button.ports}")
        if (_uiState.value.isPortScanInProgress) {
            return
        }

        viewModelScope.launch {
            dbRepo.insertHost(hostNameState.text.toString())
            dataStoreRepo.saveRecentHost(hostNameState.text.toString())
        }
        scanPorts(button.ports)
    }
}