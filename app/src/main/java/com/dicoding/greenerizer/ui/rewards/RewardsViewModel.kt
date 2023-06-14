package com.dicoding.greenerizer.ui.rewards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.greenerizer.data.api.ApiConfig
import com.dicoding.greenerizer.data.response.RewardsResponseItem
import com.dicoding.greenerizer.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class RewardsViewModel : ViewModel() {
    private val _listRewards = MutableLiveData<List<RewardsResponseItem>>()
    val listRewards: LiveData<List<RewardsResponseItem>> = _listRewards

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        getRewards()
    }

    private fun getRewards() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.getApiService().getRewards()
                if (client.isSuccessful) {
                    val responseBody = client.body()
                    if(responseBody != null) {
                        _isLoading.postValue(false)
                        _listRewards.postValue(client.body())
                        Log.d(TAG, "checkData: ${client.code()}")
                    } else {
                        _isLoading.postValue(false)
                        _snackbarText.postValue(Event("Gagal menghubungkan"))
                        Log.e(TAG, "onFailure: ${client.message()}")
                    }
                }
            } catch (e: SocketTimeoutException) {
                _isLoading.postValue(false)
                _snackbarText.postValue(Event("Tidak ada internet"))
            }

        }
    }

    companion object {
        private const val TAG = "RewardsViewModel"
    }
}