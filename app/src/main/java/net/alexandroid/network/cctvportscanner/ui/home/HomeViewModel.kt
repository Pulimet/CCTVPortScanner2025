package net.alexandroid.network.cctvportscanner.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.preferences.core.edit
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
import net.alexandroid.network.cctvportscanner.datasotre.DataStore
import net.alexandroid.network.cctvportscanner.datasotre.DataStore.getDataStoreByKey
import net.alexandroid.network.cctvportscanner.repo.DbRepo
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo

class HomeViewModel(
    private val portScanRepo: PortScanRepo,
    private val pingRepo: PingRepo,
    private val dbRepo: DbRepo,
    context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val dataStore = context.getDataStoreByKey(DataStore.Key.HOME_DATA)

    val hostNameState = TextFieldState()
    val customPortState = TextFieldState()
    var hostName = ""
    var port = ""

    init {
        viewModelScope.launch {
            dbRepo.getAllHostsFlow().collect { hosts ->
                Log.d("HomeViewModel", "Hosts from DB: ${hosts.size}")
                val hostList = hosts.map { it.hostName }
                _uiState.value = _uiState.value.copy(allHosts = hostList)
            }
        }
        viewModelScope.launch {
            dataStore.data.collectLatest { preferences ->
                preferences[DataStore.RECENT_HOST]?.let { recentHost ->
                    Log.d("HomeViewModel", "Recent host from DataStore: $recentHost")
                    hostNameState.setTextAndPlaceCursorAtEnd(recentHost)
                    onHostNameChanged(recentHost)
                }
                preferences[DataStore.RECENT_PORT]?.let { recentPort ->
                    Log.d("HomeViewModel", "Recent port from DataStore: $recentPort")
                    customPortState.setTextAndPlaceCursorAtEnd(recentPort)
                    onCustomPortChanged(recentPort)
                }
            }
        }
    }

    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

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

            dbRepo.insertHost(hostNameState.text.toString())
            dataStore.edit { preferences ->
                Log.d("HomeViewModel", "onHostPingSubmit save host to DataStore: ${hostNameState.text}")
                preferences[DataStore.RECENT_HOST] = hostNameState.text.toString()
            }
        }
    }

    @OptIn(FlowPreview::class)
    fun onPortScanSubmit() {
        if (_uiState.value.portValidStatus != Status.SUCCESS && !_uiState.value.isPortScanInProgress) {
            return
        }
        Log.d("HomeViewModel", "onPortSubmit")

        _uiState.value = _uiState.value.copy(isPortScanInProgress = true)

        viewModelScope.launch {
            dbRepo.insertHost(hostNameState.text.toString())
            dataStore.edit { preferences ->
                preferences[DataStore.RECENT_HOST] = hostNameState.text.toString()
                preferences[DataStore.RECENT_PORT] = customPortState.text.toString()
            }
        }

        val scanFlow = portScanRepo.scanPorts(
            hostNameState.text.toString(), customPortState.text.toString()
        )

        scanFlow
            .onStart { Log.d("HomeViewModel", "Port scan Started") }
            .conflate()
            .onEach { sampledUpdate ->
                Log.d("HomeViewModel", "onEach results size: ${sampledUpdate.results.size}")
                _uiState.value = _uiState.value.copy(
                    portScanResults = sampledUpdate.results, isPortScanInProgress = true
                )
                delay(500L)
            }.onCompletion {
                Log.d("HomeViewModel", "Port scan completed")
                _uiState.value = _uiState.value.copy(isPortScanInProgress = false)
            }.catch { e -> // Catch errors from the sampling part
                Log.e("HomeViewModel", "Error in sampled progress flow", e)
            }.launchIn(viewModelScope) // Launch this part as a separate collector
    }
}