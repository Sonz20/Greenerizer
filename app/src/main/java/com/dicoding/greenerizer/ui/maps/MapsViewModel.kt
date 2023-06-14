package com.dicoding.greenerizer.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.greenerizer.data.api.ApiConfig
import com.dicoding.greenerizer.data.response.LocationResponseItem
import com.dicoding.greenerizer.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MapsViewModel: ViewModel() {

    private val _listLocation =  MutableLiveData<List<LocationResponseItem>>()
    val listLocation : LiveData<List<LocationResponseItem>> = _listLocation

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        getRubbishBankLocation()
    }

    private fun getRubbishBankLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.getApiService().getRubbishBankLocation()
                if (client.isSuccessful) {
                    val responseBody = client.body()
                    if (responseBody != null) {
                        _listLocation.postValue(client.body())
                        Log.d(TAG, "onSuccess: ${client.code()}")
                    } else {
                        _snackbarText.postValue(Event("Gagal menghubungkan"))
                        Log.e(TAG, "onFailure: ${client.message()}")
                    }
                }
            } catch (e: SocketTimeoutException) {
                _snackbarText.postValue(Event("Tidak ada internet"))
            }
        }
    }

    companion object {
        private const val TAG = "MapsVM"
    }
}