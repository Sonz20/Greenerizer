package com.dicoding.greenerizer.ui.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentRedeemBinding
import com.dicoding.greenerizer.ui.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RedeemFragment : Fragment() {

    private var _binding : FragmentRedeemBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRedeemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val rewardName = RedeemFragmentArgs.fromBundle(arguments as Bundle).rewardName
        val rewardPoint = RedeemFragmentArgs.fromBundle(arguments as Bundle).rewardPrice

        setView(rewardName, rewardPoint)

        tradePoint(auth.currentUser?.uid.toString(), rewardPoint)

        binding.btnConfirm.setOnClickListener {
            view.findNavController().navigate(R.id.action_redeemFragment_to_navigation_home)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view.findNavController().navigate(R.id.action_redeemFragment_to_navigation_home)
        }
    }

    private fun setView(rewardName: String, rewardPoint: Int){
        binding.tvReward.text = rewardName
        binding.point.text = rewardPoint.toString()
    }

    private fun tradePoint(userId: String, totalPoint: Int){
        var userPoint: Int
        db = FirebaseDatabase.getInstance().getReference(RegisterFragment.USER_CHILD)
        db.child(userId).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                userPoint = dataSnapshot.child("totalPoint").value.toString().toInt()
                db.child(userId).child("totalPoint").setValue(userPoint - totalPoint)
            }
        }
    }
}