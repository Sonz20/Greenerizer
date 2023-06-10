package com.dicoding.greenerizer.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentTransactionBinding
import com.dicoding.greenerizer.ui.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val rubbishName = TransactionFragmentArgs.fromBundle(arguments as Bundle).rubbishType
        val totalUnit = TransactionFragmentArgs.fromBundle(arguments as Bundle).valueWeight
        val totalPoint = TransactionFragmentArgs.fromBundle(arguments as Bundle).totalPoint

        binding.tvRubbish.text = rubbishName
        binding.valueWeight.text = totalUnit.toString()
        binding.point.text = totalPoint.toString()

        binding.btnConfirm.setOnClickListener {
            sendPoint(auth.currentUser?.uid.toString(), totalPoint)
            view.findNavController().navigate(R.id.action_transactionFragment_to_navigation_home)
        }
    }

    private fun sendPoint(userId: String, totalPoint: Int){
        var userPoint = 0
        db = FirebaseDatabase.getInstance().getReference(RegisterFragment.USER_CHILD)
        db.child(userId).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                userPoint = dataSnapshot.child("totalPoint").value.toString().toInt()
                db.child(userId).child("totalPoint").setValue(userPoint + totalPoint)
            }
        }
    }
}