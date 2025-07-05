package net.alexandroid.network.cctvportscanner.ui.home

import net.alexandroid.network.cctvportscanner.repo.PortScanStatus
import net.alexandroid.network.cctvportscanner.room.button.ButtonEntity
import net.alexandroid.network.cctvportscanner.ui.common.Status


data class HomeUiState(
    val isPingInProgress: Boolean = false,
    val isPortScanInProgress: Boolean = false,
    val showAddButtonDialog: Boolean = false,
    val hostValidStatus: Status = Status.UNKNOWN,
    val recentPingStatus: Status = Status.UNKNOWN,
    val portValidStatus: Status = Status.UNKNOWN,
    val validPorts: String = "",
    val portScanResults: Map<Int, PortScanStatus> = emptyMap(),
    val allHosts: List<String> = emptyList(),
    val allButtons: List<ButtonEntity> = emptyList()
)
