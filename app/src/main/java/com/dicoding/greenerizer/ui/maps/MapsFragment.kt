package com.dicoding.greenerizer.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val mapsViewModel by viewModels<MapsViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        // Get a reference to the MapView
        mapView = binding.mapView

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Initialize the MapView amd display the map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->

            // Save a reference to the GoogleMap
            this.mMap = googleMap

            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isMapToolbarEnabled = true

            // Configure and display the map
            getMyLocation()
            getRubbishBankLocation()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsViewModel.snackbarText.observe(viewLifecycleOwner) {
            val contextView = requireActivity().findViewById<View>(R.id.container)
            it.getContentIfNotHandled()?.let { text ->
                if(text.isNotEmpty()) {
                    Snackbar.make(
                        contextView,
                        text,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getRubbishBankLocation() {
        mapsViewModel.listLocation.observe(viewLifecycleOwner) {
            it.forEach { data ->
                val location = LatLng(data.latitude.toDouble(), data.longitude.toDouble())
                val nameLocation = data.namabanksampah.ifEmpty {
                    resources.getString(R.string.rubbish_bank)
                }
                mMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(nameLocation)
                )
            }
        }
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}