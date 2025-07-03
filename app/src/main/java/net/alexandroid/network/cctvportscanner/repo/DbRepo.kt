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

    suspend fun deleteButton(buttonEntity: ButtonEntity) {
        buttonDao.delete(buttonEntity)
    }

    fun getAllButtonsFlow() = buttonDao.getAllItemsFlow()

    suspend fun loadDefaultButtons() {
        buttonDao.insertAll(
            listOf(
                ButtonEntity(title = "80", ports = "80"),
                ButtonEntity(title = "90", ports = "90"),
                ButtonEntity(title = "8080", ports = "8080"),
                ButtonEntity(title = "Geovision DVR/NVR", ports = "80,4550,5550,6550,5552,8866,5511"),
                ButtonEntity(title = "Geovision CenterV2", ports = "5547"),
                ButtonEntity(title = "Geovision IP Device", ports = "80,5552,10000"),
                ButtonEntity(title = "Rifatron", ports = "80,2000,50100"),
                ButtonEntity(title = "Procam", ports = "80,90"),
                ButtonEntity(title = "Avigilon", ports = "38880-38883,80,50081-50083"),
                ButtonEntity(title = "Evermedia", ports = "0,5555"),
                ButtonEntity(title = "Win4Net", ports = "80, 9010, 2000"),
                ButtonEntity(title = "Sentinel", ports = "80,8000,9000"),
                ButtonEntity(title = "Provision", ports = "80, 8000"),
                ButtonEntity(title = "Dahua", ports = "80, 37777, 37778"),
                ButtonEntity(title = "Avtech", ports = "80")
            )
        )
    }
}