package net.alexandroid.network.cctvportscanner.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress

class PingRepo {
    suspend fun pingHost(host: String, timeout: Int = 5000) = withContext(Dispatchers.IO) {
        try {
            InetAddress.getByName(host).isReachable(timeout)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}