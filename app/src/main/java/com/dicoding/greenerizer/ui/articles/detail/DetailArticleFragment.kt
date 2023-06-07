package com.dicoding.greenerizer.ui.articles.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentDetailArticleBinding

class DetailArticleFragment : Fragment() {

    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = DetailArticleFragmentArgs.fromBundle(arguments as Bundle).image
        val rubbishType = DetailArticleFragmentArgs.fromBundle(arguments as Bundle).rubbishType
        if (rubbishType == "trash") {
            binding.trashType.let {
                it.text = resources.getString(R.string.organic)
                it.setBackgroundResource(R.drawable.bg_organic)
            }
        }
        val description = DetailArticleFragmentArgs.fromBundle(arguments as Bundle).description
        val price = DetailArticleFragmentArgs.fromBundle(arguments as Bundle).price
        val handleRubbish = DetailArticleFragmentArgs.fromBundle(arguments as Bundle).tipsHandle

        setDetail(imageUrl, rubbishType, description, price, handleRubbish)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setDetail(image: String, rubbishType: String, description: String, price: Int, handleRubbish: String) {
        Glide.with(this)
            .load(image)
            .into(binding.ivRubbish)
        binding.titleRubbish.text = rubbishType
        binding.priceTrash.text = price.toString()
        binding.tvDescription.text = description
        binding.tvRubbish.text = handleRubbish
    }

}