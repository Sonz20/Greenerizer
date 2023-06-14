package com.dicoding.greenerizer.ui.articles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.greenerizer.data.api.ApiConfig
import com.dicoding.greenerizer.data.response.RubbishResponseItem
import com.dicoding.greenerizer.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ArticlesViewModel: ViewModel() {

    private val _listArticles = MutableLiveData<List<RubbishResponseItem>>()
    val listArticles: LiveData<List<RubbishResponseItem>> = _listArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        getRubbish()
    }

    private fun getRubbish() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = ApiConfig.getApiService().getRubbish()
                if (client.isSuccessful) {
                    val responseBody = client.body()
                    if(responseBody != null) {
                        _isLoading.postValue(false)
                        _listArticles.postValue(client.body())
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
        private const val TAG = "ArticlesViewModel"
    }
}