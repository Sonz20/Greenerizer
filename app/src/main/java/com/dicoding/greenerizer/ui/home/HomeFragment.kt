package com.dicoding.greenerizer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.protobuf.Value
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.greenerizer.UserViewModel
import com.dicoding.greenerizer.data.local.User
import com.dicoding.greenerizer.data.local.UserPreferences
import com.dicoding.greenerizer.dataStore
import com.dicoding.greenerizer.databinding.FragmentHomeBinding
import com.dicoding.greenerizer.helper.ViewModelFactory
import com.dicoding.greenerizer.ui.register.RegisterFragment
import com.dicoding.greenerizer.ui.register.RegisterFragment.Companion.USER_CHILD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        Log.d("LoginInfo", auth.currentUser?.uid.toString())
        getUserInfo(auth.currentUser?.uid.toString())

    }
    private fun getUserInfo(userId: String) {
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]

        db = FirebaseDatabase.getInstance().getReference(USER_CHILD)
        db.child(userId).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                Log.d("LoginInfo", dataSnapshot.toString())
                val name = dataSnapshot.child("name").value
                val point = dataSnapshot.child("totalPoint").value
                Log.d("LoginInfo", name.toString())
                binding.nameGreetings.text = name.toString()
                binding.pointValue.text = point.toString()
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}