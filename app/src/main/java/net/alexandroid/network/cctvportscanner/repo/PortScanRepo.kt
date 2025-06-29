package net.alexandroid.network.cctvportscanner.repo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import net.alexandroid.network.cctvportscanner.ui.home.Status
import net.alexandroid.network.cctvportscanner.utils.PortUtils
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

enum class PortScanStatus {
    OPEN,
    WRONG_HOST,
    TIMEOUT,
    CLOSED
}

private const val TIMEOUT_IN_MS = 2000

data class ScanUpdate(val results: Map<Int, PortScanStatus>, val isScanInProgress: Boolean)

class PortScanRepo {
    var scanResults = ConcurrentHashMap<Int, PortScanStatus>()
    var isScanInProgress = false

    @OptIn(ExperimentalCoroutinesApi::class)
    fun scanPorts(host: String, ports: String): Flow<ScanUpdate> = callbackFlow {
        scanResults.clear()
        isScanInProgress = true

        val portList = PortUtils.convertStringToIntegerList(ports.trim())

        val maxConcurrentScans = PortUtils.getRecommendedMaxConcurrentScans()
        Log.d("PortScanRepo", "maxConcurrentScans: $maxConcurrentScans")
        val dispatcher = Dispatchers.IO.limitedParallelism(maxConcurrentScans)
        val parentJob = Job()

        portList.forEach { port ->
            launch(dispatcher + parentJob) {
                val status = scanPort(host, port)
                scanResults[port] = status
                isScanInProgress = portList.size != scanResults.size
                trySend(ScanUpdate(scanResults, isScanInProgress))
                if (!isScanInProgress) {
                    close() // Close the flow when scanning is done
                }
            }
        }

        awaitClose {
            Log.d("PortScanRepo", "awaitClose called for $host. Cleaning up scan...")
            parentJob.cancel() // Cancel all launched scan jobs if the flow is cancelled
        }

    }

    fun scanPort(host: String, port: Int): PortScanStatus {
        var state: PortScanStatus
        try {
            val socket = Socket()
            val address = InetSocketAddress(host, port)
            socket.connect(address, TIMEOUT_IN_MS)
            state = if (socket.isConnected && socket.isBound) {
                PortScanStatus.OPEN
            } else {
                PortScanStatus.CLOSED
            }
            socket.close()
        } catch (_: java.net.UnknownHostException) {
            state = PortScanStatus.WRONG_HOST
        } catch (_: java.net.SocketTimeoutException) {
            state = PortScanStatus.TIMEOUT
        } catch (_: java.io.IOException) {
            state = PortScanStatus.CLOSED
        }
        // Log.d("HomeViewModel", "Host: $host -> Port $port state: ${state.name}")

        return state
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