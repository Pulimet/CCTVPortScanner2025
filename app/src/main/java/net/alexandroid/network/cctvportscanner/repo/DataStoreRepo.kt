package net.alexandroid.network.cctvportscanner.repo

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.collectLatest
import net.alexandroid.network.cctvportscanner.datasotre.DataStore
import net.alexandroid.network.cctvportscanner.datasotre.DataStore.getDataStoreByKey

class DataStoreRepo(context: Context) {

    val dataStore = context.getDataStoreByKey(DataStore.Key.HOME_DATA)

    suspend fun isDefaultButtonsLoaded(callback: (isDefaultsLoaded: Boolean) -> Unit) {
        dataStore.data.collectLatest { preferences ->
            val isDataLoaded = preferences[DataStore.DEFAULT_DATA_LOADED]
            callback.invoke(isDataLoaded == true)
        }
    }

    suspend fun setDefaultDataLoaded() {
        dataStore.edit { preferences ->
            Log.d("HomeViewModel", "Set default data loaded in DataStore")
            preferences[DataStore.DEFAULT_DATA_LOADED] = true
        }
    }

    suspend fun getRecentHostAndPort(callback: (data: Pair<String, String>) -> Unit) {
        dataStore.data.collectLatest { preferences ->
            val recentHost = preferences[DataStore.RECENT_HOST]
            val recentPort = preferences[DataStore.RECENT_PORT]
            callback.invoke(Pair(recentHost ?: "", recentPort ?: ""))
        }
    }

    suspend fun saveRecentHost(host: String) {
        dataStore.edit { preferences ->
            Log.d("HomeViewModel", "onHostPingSubmit save host to DataStore: $host")
            preferences[DataStore.RECENT_HOST] = host
        }
    }

    suspend fun saveRecentPort(port: String) {
        dataStore.edit { preferences ->
            Log.d("HomeViewModel", "Save port to DataStore: $port")
            preferences[DataStore.RECENT_PORT] = port
        }
    }
}