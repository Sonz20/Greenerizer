package com.dicoding.greenerizer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentHomeBinding
import com.dicoding.greenerizer.ui.register.RegisterFragment.Companion.USER_CHILD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        getUserInfo(auth.currentUser?.uid.toString())

        binding.boxFunction.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_home_to_navigation_account)
        }
    }
    private fun getUserInfo(userId: String) {
        binding.progressBar.visibility = View.VISIBLE
        db = FirebaseDatabase.getInstance().getReference(USER_CHILD)
        db.child(userId).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                val name = dataSnapshot.child("name").value
                val point = dataSnapshot.child("totalPoint").value
                binding.nameGreetings.text = name.toString()
                binding.pointValue.text = point.toString()
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}