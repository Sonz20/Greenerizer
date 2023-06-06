package com.dicoding.greenerizer.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

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

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        auth = Firebase.auth

        getUserInfo(auth.currentUser?.uid.toString())

        binding.boxFunction.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.boxFunction)
            popupMenu.menuInflater.inflate(R.menu.photo_menu, popupMenu.menu)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true)
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.camera -> startCamera(view)
                    R.id.gallery -> startGallery()
                }
                true
            }
            popupMenu.show()
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

    private fun startCamera(view: View) {
        view.findNavController().navigate(R.id.action_navigation_home_to_cameraFragment)
    }

    private fun startGallery() {
        Toast.makeText(requireContext(), "Gallery Open", Toast.LENGTH_SHORT).show()
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGalley.launch(chooser)
    }

    private val launcherIntentGalley = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val toResultFragment = HomeFragmentDirections.actionNavigationHomeToResultFragment(null, false, selectedImg)
            view?.findNavController()?.navigate(toResultFragment)
        }
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}