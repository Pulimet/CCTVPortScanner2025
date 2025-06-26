package net.alexandroid.network.cctvportscanner.ui.home

enum class PingStatus {
    SUCCESS,
    FAILURE,
    UNKNOWN
}

data class HomeUiState(
    val isPingInProgress: Boolean = false,
    val recentPingStatus: PingStatus = PingStatus.UNKNOWN
)
