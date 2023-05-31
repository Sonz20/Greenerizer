package com.dicoding.greenerizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.greenerizer.data.local.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreferences): ViewModel() {

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            pref.saveName(name)
        }
    }

    fun getEmail(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            pref.saveEmail(email)
        }
    }

    fun getUserId(): LiveData<String> {
        return pref.getUserId().asLiveData()
    }

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            pref.saveUserId(userId)
        }
    }
}