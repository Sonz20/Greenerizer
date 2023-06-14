package com.dicoding.greenerizer.ui.rewards.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.greenerizer.databinding.FragmentDetailRewardsBinding
import com.dicoding.greenerizer.ui.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DetailRewardsFragment : Fragment() {
    private val args: DetailRewardsFragmentArgs by navArgs()
    private var _binding: FragmentDetailRewardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val image = args.image
        val rewards = args.rewards
        val voucher = args.voucher
        val descriptions = args.descriptions

        setDetail(image, rewards, descriptions, voucher)

        db = FirebaseDatabase.getInstance().getReference(RegisterFragment.USER_CHILD)
        db.child(auth.currentUser?.uid.toString()).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val dataSnapshot = it.result
                val point = dataSnapshot.child("totalPoint").value
                setButtonEnabled(point.toString().toInt())
            }
        }

        binding.backButton.setOnClickListener {
            @Suppress("DEPRECATION")
            requireActivity().onBackPressed()
        }

        binding.btnDebit.setOnClickListener {
            val toRedeem = DetailRewardsFragmentDirections.actionDetailRewardsFragmentToRedeemFragment(
                rewards, voucher
            )
            view.findNavController().navigate(toRedeem)
        }
    }

    private fun setDetail(image: String, rewards: String, descriptions: String, voucher: Int) {
        Glide.with(requireContext())
            .load(image)
            .into(binding.ivImage)
        binding.nameRewards.text = rewards
        binding.valuePoints.text = voucher.toString()
        binding.tvDescription.text = descriptions
    }
    private fun setButtonEnabled(userPoint: Int) {
        val pointReward = binding.valuePoints.text.toString().toInt()
        binding.btnDebit.isEnabled = pointReward <= userPoint
    }

}
