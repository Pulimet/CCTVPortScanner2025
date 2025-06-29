package net.alexandroid.network.cctvportscanner.repo

import net.alexandroid.network.cctvportscanner.room.HostDao
import net.alexandroid.network.cctvportscanner.room.HostEntity

class DbRepo(private val hostDao: HostDao) {
    suspend fun insertHost(host: String) {
        hostDao.insert(HostEntity(hostName = host))
    }

    suspend fun deleteHost(host: HostEntity) {
        hostDao.delete(host)
    }

    suspend fun getAllHosts() = hostDao.getAllItems()

    fun getAllHostsFlow() = hostDao.getAllItemsFlow()
}