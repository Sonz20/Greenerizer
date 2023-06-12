package com.dicoding.greenerizer.ui.rewards.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.greenerizer.databinding.FragmentDetailRewardsBinding

class DetailRewardsFragment : Fragment() {
    private val args: DetailRewardsFragmentArgs by navArgs()
    private var _binding: FragmentDetailRewardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = args.image
        val rewards = args.rewards
        val voucher = args.voucher
        val descriptions = args.descriptions

        setDetail(image, rewards, descriptions, voucher)

        binding.backButton.setOnClickListener {
            @Suppress("DEPRECATION")
            requireActivity().onBackPressed()
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
}
