package com.dicoding.greenerizer.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.greenerizer.data.api.ApiConfig
import com.dicoding.greenerizer.data.response.LocationResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel: ViewModel() {

    private val _listLocation =  MutableLiveData<List<LocationResponseItem>>()
    val listLocation : LiveData<List<LocationResponseItem>> = _listLocation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getRubbishBankLocation()
    }

    private fun getRubbishBankLocation() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val client = ApiConfig.getApiService().getRubbishBankLocation()
            if (client.isSuccessful) {
                val responseBody = client.body()
                if (responseBody != null) {
                    _isLoading.postValue(false)
                    _listLocation.postValue(client.body())
                    Log.d(TAG, "onSuccess: ${client.code()}")
                } else {
                    _isLoading.postValue(false)
                    Log.e(TAG, "onFailure: ${client.message()}")
                }
            }
        }
    }

    companion object {
        private const val TAG = "MapsVM"
    }
}