package net.alexandroid.network.cctvportscanner.datasotre

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStore {
    enum class Key {
        HOME_DATA,
    }

    fun Context.getDataStoreByKey(key: Key): DataStore<Preferences> {
        return when (key) {
            Key.HOME_DATA -> recentHosts
        }
    }

    private val Context.recentHosts: DataStore<Preferences> by preferencesDataStore(name = Key.HOME_DATA.name)

    val RECENT_HOST = stringPreferencesKey("recent_host")
    val RECENT_PORT = stringPreferencesKey("recent_PORT")
}