package com.dicoding.greenerizer.ui.profile

import android.os.Bundle
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
import com.dicoding.greenerizer.databinding.FragmentProfileBinding
import com.dicoding.greenerizer.helper.ViewModelFactory
import com.dicoding.greenerizer.ui.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        getUserInfo(auth.currentUser?.uid.toString())

        binding.btnLogout.setOnClickListener {
            signOut()
        }
    }

    private fun getUserInfo(userId: String) {
        db = FirebaseDatabase.getInstance().getReference(RegisterFragment.USER_CHILD)
        db.child(userId).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                val name = dataSnapshot.child("name").value
                val email = dataSnapshot.child("email").value
                binding.tvName.text = name.toString()
                binding.tvEmail.text = email.toString()
            }
        }
    }

    private fun signOut() {
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]

        auth.signOut()
        userViewModel.clearData()
        view?.findNavController()?.navigate(R.id.action_profileFragment_to_SplashScreenFragment)
    }
}