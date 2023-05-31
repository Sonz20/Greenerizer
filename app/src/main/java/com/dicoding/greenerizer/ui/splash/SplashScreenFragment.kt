package com.dicoding.greenerizer.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.UserViewModel
import com.dicoding.greenerizer.data.local.UserPreferences
import com.dicoding.greenerizer.dataStore
import com.dicoding.greenerizer.helper.ViewModelFactory

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]

        val delay: Long = 3000
        Handler(Looper.getMainLooper()).postDelayed({
            userViewModel.getUserId().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    view.findNavController()
                        .navigate(R.id.action_SplashScreenFragment_to_navigation_home)
                } else {
                    view.findNavController()
                        .navigate(R.id.action_SplashScreenFragment_to_registerFragment)
                }
            }
        }, delay)
    }

}