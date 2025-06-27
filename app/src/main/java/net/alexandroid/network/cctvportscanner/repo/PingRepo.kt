package net.alexandroid.network.cctvportscanner.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PingRepo {

    suspend fun pingHost(host: String, timeoutMillis: Long = 5000) = withContext(Dispatchers.IO) {
        try {
            val runtime = Runtime.getRuntime()
            val command = "/system/bin/ping -c 1 -w ${timeoutMillis / 1000} $host"
            val process = runtime.exec(command)
            val exited = process.waitFor(timeoutMillis, TimeUnit.MILLISECONDS)
            if (exited) {
                process.exitValue() == 0
            } else {
                process.destroy()
                false
            }
        } catch (_: Exception) {
            false
        }
    }
}