package net.alexandroid.network.cctvportscanner.ui.home

import net.alexandroid.network.cctvportscanner.repo.PortScanStatus
import net.alexandroid.network.cctvportscanner.room.HostEntity

enum class Status {
    SUCCESS,
    FAILURE,
    UNKNOWN
}

data class HomeUiState(
    val isPingInProgress: Boolean = false,
    val isPortScanInProgress: Boolean = false,
    val hostValidStatus: Status = Status.UNKNOWN,
    val recentPingStatus: Status = Status.UNKNOWN,
    val portValidStatus: Status = Status.UNKNOWN,
    val validPorts: String = "",
    val portScanResults: Map<Int, PortScanStatus> = emptyMap(),
    val allHosts: List<String> = emptyList()
)
