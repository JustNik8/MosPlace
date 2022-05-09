package com.justnik.mosplace.presentation.place

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.helpers.hideSupportActionBar
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.showSupportActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceFragment : Fragment(R.layout.fragment_place) {

    private val args: PlaceFragmentArgs by navArgs()

    private val locationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        this::onGotPermissionsLocationResult
    )

    private val binding: FragmentPlaceBinding by viewBinding()

    private val place by lazy {
        args.place
    }

    private val viewModel: PlaceViewModel by viewModels()

    private var currentLocation: Location? = null

    private val locationManager by lazy {
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val gpsLocationListener = LocationListener { location ->
        currentLocation = location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSupportActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setCurrentPlace(place)
        setupPlaceText()
        setupImageSlider()
        setClickListeners()
        observeEvents()

        setCheckInButton {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions() {
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

        locationPermissionRequestLauncher.launch(
            arrayOf(coarseLocationPermission, fineLocationPermission)
        )
    }

    private fun observeEvents() {
        viewModel.checkInEvents.observeFlow(viewLifecycleOwner) { event ->
            when (event) {
                is PlaceViewModel.CheckInEvent.Success -> {
                    navigateToReviewFragment()
                }
                is PlaceViewModel.CheckInEvent.Error -> {
                    Toast.makeText(
                        requireContext(),
                        event.errorMessage.asString(requireContext()),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is PlaceViewModel.CheckInEvent.Review -> {
                    navigateToReviewFragment()
                }
            }
        }

        viewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            binding.bCheckIn.icon = when {
                uiState.isLoading -> {
                    null
                }
                uiState.isPlaceAlreadyVisited -> {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_rate_review)
                }
                else -> {
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_check)
                }
            }

            binding.pbPlace.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
            binding.bCheckIn.isEnabled = !uiState.isLoading && viewModel.isUserAuthorized()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        showSupportActionBar()
    }


    @SuppressLint("MissingPermission")
    private fun onGotPermissionsLocationResult(grantResult: Map<String, Boolean>) {
        if (grantResult.entries.all { it.value }) {

            val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    0F,
                    gpsLocationListener
                )
            }

            setCheckInButton{
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                viewModel.checkIn(place, currentLocation)
            }
            binding.bCheckIn.performClick()
        } else {
            Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_LONG).show()
            setCheckInButton{
                checkLocationPermissions()
            }
        }
    }

    private fun setCheckInButton(onClickAction: () -> Unit) {
        binding.bCheckIn.setOnClickListener {
            onClickAction()
        }
    }

    private fun setupPlaceText() {
        with(binding) {
            tvPlaceTitle.text = place.title
            tvPlaceDesc.text = place.fullDescription.repeat(10)
        }
    }

    private fun setupImageSlider() {
        val imageUrls = place.images.map { it.imageUrl }
        val placeImageAdapter = PlaceImageSliderAdapter(imageUrls, requireContext())
        binding.placeImageSlider.setSliderAdapter(placeImageAdapter)
    }

    private fun setClickListeners() {
        binding.bLocatePlace.setOnClickListener {
            viewModel.openPlaceInMap(place)
        }

        binding.bBackPlace.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToReviewFragment() {
        findNavController().navigate(R.id.action_placeFragment_to_reviewFragment)
    }

}