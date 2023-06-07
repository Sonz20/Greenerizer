package com.dicoding.greenerizer.ui.articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.greenerizer.adapter.RubbishAdapter
import com.dicoding.greenerizer.data.response.RubbishResponseItem
import com.dicoding.greenerizer.databinding.FragmentArticlesBinding

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val articlesViewModel by viewModels<ArticlesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerList()

        articlesViewModel.listArticles.observe(viewLifecycleOwner) {
            setRubbish(it)
        }

        articlesViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRubbish.layoutManager = layoutManager
    }

    private fun setRubbish(rubbishItem: List<RubbishResponseItem>) {
        val adapter = RubbishAdapter(rubbishItem)
        binding.rvRubbish.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}