package net.alexandroid.network.cctvportscanner.repo

import net.alexandroid.network.cctvportscanner.room.button.ButtonDao
import net.alexandroid.network.cctvportscanner.room.button.ButtonEntity
import net.alexandroid.network.cctvportscanner.room.host.HostDao
import net.alexandroid.network.cctvportscanner.room.host.HostEntity

class DbRepo(private val hostDao: HostDao, private val buttonDao: ButtonDao) {

    // HostDao
    suspend fun insertHost(host: String) {
        hostDao.insert(HostEntity(hostName = host))
    }

    suspend fun deleteHost(host: String) {
        hostDao.delete(HostEntity(hostName = host))
    }

    fun getAllHostsFlow() = hostDao.getAllItemsFlow()

    // BtnDao
    suspend fun insertButton(title: String, ports: String) {
        buttonDao.insert(ButtonEntity(title = title, ports = ports))
    }

    suspend fun deleteButton(title: String, ports: String) {
        buttonDao.delete(ButtonEntity(title = title, ports = ports))
    }

    fun getAllButtonsFlow() = buttonDao.getAllItemsFlow()
}