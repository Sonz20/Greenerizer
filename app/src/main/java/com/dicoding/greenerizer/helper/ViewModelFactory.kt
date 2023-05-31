package com.dicoding.greenerizer.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.greenerizer.UserViewModel
import com.dicoding.greenerizer.data.local.UserPreferences

class ViewModelFactory(private val pref: UserPreferences) :ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }
}