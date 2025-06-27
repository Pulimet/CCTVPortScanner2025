package net.alexandroid.network.cctvportscanner.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.network.cctvportscanner.ui.home.Status
import net.alexandroid.network.cctvportscanner.utils.PortUtils
import java.net.InetSocketAddress
import java.net.Socket

enum class PortScanStatus {
    INIT,
    OPEN,
    WRONG_HOST,
    TIMEOUT,
    CLOSED
}

private const val TIMEOUT_IN_MS = 2000

class PortScanRepo {
    var scanResults = mutableMapOf<Int, PortScanStatus>()
    var isScanInProgress = false

    suspend fun scanPorts(host: String, ports: String, callback: (Map<Int, PortScanStatus>, isScanInProgress: Boolean) -> Unit) =
        withContext(Dispatchers.IO) {
            scanResults.clear()
            isScanInProgress = true
            val portList = PortUtils.convertStringToIntegerList(ports.trim())
            portList.forEach {
                launch {
                    scanPort(host, it) { port, status ->
                        scanResults[port] = status
                        isScanInProgress = portList.size != scanResults.size
                        callback(scanResults, isScanInProgress)
                    }
                }
            }
        }

    fun scanPort(host: String, port: Int, callback: (port: Int, status: PortScanStatus) -> Unit) {
        var state: PortScanStatus
        try {
            val socket = Socket()
            val address = InetSocketAddress(host, port)
            socket.connect(address, TIMEOUT_IN_MS)
            if (socket.isConnected && socket.isBound) {
                state = PortScanStatus.OPEN
            } else {
                state = PortScanStatus.CLOSED
            }
            socket.close()
        } catch (_: java.net.UnknownHostException) {
            state = PortScanStatus.WRONG_HOST
        } catch (_: java.net.SocketTimeoutException) {
            state = PortScanStatus.TIMEOUT
        } catch (_: java.io.IOException) {
            state = PortScanStatus.CLOSED
        }

        callback(port, state)
    }

    fun validateHost(host: String, callback: (status: Status) -> Unit) {
        if (host.trim().isEmpty() || host.trim().length < 7) {
            callback.invoke(Status.UNKNOWN)
            return
        }
        val parts = host.trim().split(".")

        if (host[0].isDigit()) {
            if (parts.size != 4) {
                callback.invoke(Status.FAILURE)
                return
            }
            try {
                parts.forEach { part ->
                    if (part.toIntOrNull() == null || part.toInt() !in 0..255) {
                        callback.invoke(Status.FAILURE)
                        return
                    }
                }
                callback.invoke(Status.SUCCESS)
            } catch (_: NumberFormatException) {
                callback.invoke(Status.FAILURE)
            }

        } else {
            if (parts.size < 2 || parts.last().length < 2) {
                callback.invoke(Status.FAILURE)
                return
            }
        }
        callback.invoke(Status.SUCCESS)
    }

    fun validatePort(ports: String, callback: (status: Status, validPorts: String?) -> Unit) {
        if (ports.trim().isEmpty()) {
            callback.invoke(Status.UNKNOWN, null)
            return
        }
        val portList = PortUtils.convertStringToIntegerList(ports.trim())
        if (portList.isEmpty()) {
            callback.invoke(Status.FAILURE, null)
        } else {
            val validPorts = PortUtils.convertIntegerListToString(portList)
            callback.invoke(Status.SUCCESS, validPorts)
        }
    }
}