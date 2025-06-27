package net.alexandroid.network.cctvportscanner.repo

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
        } catch (e: java.net.UnknownHostException) {
            state = PortScanStatus.WRONG_HOST
        } catch (e: java.net.SocketTimeoutException) {
            state = PortScanStatus.TIMEOUT
        } catch (e: java.io.IOException) {
            state = PortScanStatus.CLOSED
        }

        scanResult.onResult(host, port, state)
    }
}