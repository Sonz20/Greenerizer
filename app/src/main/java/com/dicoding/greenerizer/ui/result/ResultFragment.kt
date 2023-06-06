package com.dicoding.greenerizer.ui.result

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentResultBinding
import com.dicoding.greenerizer.helper.rotateFile
import com.dicoding.greenerizer.helper.uriToFile
import java.io.File


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        getFile = ResultFragmentArgs.fromBundle(arguments as Bundle).picture
        val selectedImage = ResultFragmentArgs.fromBundle(arguments as Bundle).imageSelected
        val isBackCamera = ResultFragmentArgs.fromBundle(arguments as Bundle).isBackCamera

        getFile?.let { file ->
            rotateFile(file, isBackCamera)
            val result = BitmapFactory.decodeFile(file.path)
            binding.previewImageView.setImageBitmap(result)
        }
        selectedImage?.let { uri ->
            val galleryResult = uriToFile(uri, requireContext())
            getFile = galleryResult
            binding.previewImageView.setImageURI(uri)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}