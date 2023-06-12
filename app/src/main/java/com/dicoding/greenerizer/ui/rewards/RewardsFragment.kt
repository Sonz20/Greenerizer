package com.dicoding.greenerizer.ui.rewards


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.greenerizer.adapter.RewardsAdapter
import com.dicoding.greenerizer.data.response.RewardsResponseItem
import com.dicoding.greenerizer.databinding.FragmentRewardsBinding


class RewardsFragment : Fragment() {

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    private val rewardsViewModel by viewModels<RewardsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPoint = RewardsFragmentArgs.fromBundle(arguments as Bundle).userPoint

        binding.valuePoint.text = userPoint.toString()

        showRecyclerList()

        rewardsViewModel.listRewards.observe(viewLifecycleOwner) {
            setRewards(it)
        }

        rewardsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun showRecyclerList() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRewards.layoutManager = layoutManager
    }

    private fun setRewards(rewardsItem: List<RewardsResponseItem>) {
        val adapter = RewardsAdapter(rewardsItem)
        binding.rvRewards.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}