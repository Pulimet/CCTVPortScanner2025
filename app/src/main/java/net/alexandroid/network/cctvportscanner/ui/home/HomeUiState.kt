package net.alexandroid.network.cctvportscanner.ui.home

enum class Status {
    SUCCESS,
    FAILURE,
    UNKNOWN
}

data class HomeUiState(
    val isPingInProgress: Boolean = false,
    val hostValidStatus: Status = Status.UNKNOWN,
    val recentPingStatus: Status = Status.UNKNOWN,
    val portValidStatus: Status = Status.UNKNOWN
)
