package com.dicoding.greenerizer.ui.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentResultBinding
import com.dicoding.greenerizer.helper.rotateFile
import com.dicoding.greenerizer.helper.uriToFile
import com.dicoding.greenerizer.ml.Model
import com.dicoding.greenerizer.ui.articles.ArticlesViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private var bitmap: Bitmap? = null
    private val imageSize = 224
    private val articlesViewModel by viewModels<ArticlesViewModel>()

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
            it.findNavController().navigate(R.id.action_resultFragment_to_navigation_home)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view.findNavController().navigate(R.id.action_resultFragment_to_navigation_home)
        }

        getFile = ResultFragmentArgs.fromBundle(arguments as Bundle).picture
        val selectedImage = ResultFragmentArgs.fromBundle(arguments as Bundle).imageSelected
        val isBackCamera = ResultFragmentArgs.fromBundle(arguments as Bundle).isBackCamera

        binding.progressBar.visibility = View.VISIBLE

        getFile?.let { file ->
            rotateFile(file, isBackCamera)
            val result = BitmapFactory.decodeFile(file.path)
            bitmap = Bitmap.createScaledBitmap(result, imageSize, imageSize, false)
            getRubbish(bitmap)
            binding.previewImageView.setImageBitmap(result)
            binding.progressBar.visibility = View.GONE
        }
        selectedImage?.let { uri ->
            val galleryResult = uriToFile(uri, requireContext())
            rotateFile(galleryResult, isBackCamera)
            val result = BitmapFactory.decodeFile(galleryResult.path)
            bitmap = Bitmap.createScaledBitmap(result, imageSize, imageSize, false)
            getRubbish(bitmap)
            getFile = galleryResult
            binding.previewImageView.setImageURI(uri)
            binding.progressBar.visibility = View.GONE
        }

        binding.btnDebit.setOnClickListener {
            val toTransactionFragment = ResultFragmentDirections.actionResultFragmentToTransactionFragment(
                binding.valueTrash.text.toString().toInt(), binding.resultScan.text.toString(), binding.totalPrice.text.toString().toInt()
            )
            view.findNavController().navigate(toTransactionFragment)
        }
    }

    @Suppress("DEPRECATION")
    private fun getRubbish(bitmap: Bitmap?) {
        // Add labels for machine learning result
        val labels = requireActivity().application.assets.open("labels.txt").bufferedReader().readLines()

        // Implement Model
        val rubbishModel = Model.newInstance(requireContext())

        // Image processing
        // convert bitmap to TensorBuffers
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        bitmap!!.getPixels(
            intValues,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )
        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(((`val` shr 16) and 0xFF) * (1.0F / 1))
                byteBuffer.putFloat(((`val` shr 8) and 0xFF) * (1.0F / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1.0F / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = rubbishModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxIdx = 0
        outputFeature0.forEachIndexed { index, fl ->
            if(outputFeature0[maxIdx] < fl) {
                maxIdx = index
            }
        }

        var result = ""

        when(labels[maxIdx]) {
            "organic" -> result = "Sampah Organik"
            "cardboard" -> result = "Kardus"
            "glass" -> result = "Botol Kaca"
            "metal" -> result = "Besi"
            "paper" -> result = "Kertas"
            "plastic" -> result = "Botol Plastik"
        }

        articlesViewModel.listArticles.observe(viewLifecycleOwner) {
            it.forEach { data ->
                if (result == data.jenisSampah) {
                    binding.resultScan.text = data.jenisSampah
                    binding.priceTrash.text = data.hargaPerkiloSampah.toString()
                    binding.tvDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(data.deskripsiSampah,  Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(data.deskripsiSampah)
                    }
                    data.hargaPerkiloSampah?.let { price -> calculationRubbishPrice(price) }
                }
            }
        }

        rubbishModel.close()
    }

    private fun calculationRubbishPrice(priceTrash: Int) {
        var unit = binding.valueTrash.text.toString().toInt()
        binding.add.setOnClickListener {
            unit++
            binding.valueTrash.text = unit.toString()
            var total = unit * priceTrash
            binding.totalPrice.text = total.toString()
            if (unit > 0) {
                binding.minus.isClickable = true
                binding.minus.setOnClickListener {
                    unit--
                    if (unit <= 0) {
                        binding.minus.isClickable = false
                    }
                    binding.valueTrash.text = unit.toString()
                    total = unit * priceTrash
                    binding.totalPrice.text = total.toString()
                }
            } else {
                binding.minus.isClickable = false
            }
        }
    }
}