package net.alexandroid.network.cctvportscanner.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import net.alexandroid.network.cctvportscanner.repo.PingRepo
import net.alexandroid.network.cctvportscanner.repo.PortScanRepo

class HomeViewModel(private val portScanRepo: PortScanRepo, private val pingRepo: PingRepo) : ViewModel() {

    fun onCreate() {
        Log.d("HomeViewModel", "onCreate called $this")
    }

    fun onTextChanged(text: String) {
        Log.d("HomeViewModel", "onTextChanged called $this (text: $text)")
    }

}