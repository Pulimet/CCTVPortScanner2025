package net.alexandroid.network.cctvportscanner.repo

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

interface ScanResult {
    fun onResult(host: String, port: Int, portScanStatus: PortScanStatus)
}

private const val TIMEOUT_IN_MS = 2000

class PortScanRepo {
    var state = PortScanStatus.INIT

    fun scanPort(host: String, port: Int, scanResult: ScanResult) {
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

        scanResult.onResult(host, port, state)
    }

    fun validatePort(ports: String, callback: (status: Status) -> Unit) {
        if (ports.trim().isEmpty()) {
            callback.invoke(Status.UNKNOWN)
            return
        }
        val portList = PortUtils.convertStringToIntegerList(ports.trim())
        if (portList.isEmpty()) {
            callback.invoke(Status.FAILURE)
        } else {
            callback.invoke(Status.SUCCESS)
        }
    }
}