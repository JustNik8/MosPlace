package com.justnik.mosplace.presentation.place

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.helpers.hideSupportActionBar
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.showSnackbar
import com.justnik.mosplace.helpers.showSupportActionBar
import com.justnik.mosplace.presentation.placereviews.PlaceReviewsAdapter
import com.justnik.mosplace.presentation.placereviews.PlaceReviewsViewModel
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

    private val reviewRvAdapter by lazy { PlaceReviewsAdapter(requireContext()) }

    private var reviews: Array<Review>? = null

    private val placeViewModel: PlaceViewModel by viewModels()
    private val reviewsViewModel: PlaceReviewsViewModel by viewModels()

    private var currentLocation: Location? = null

    private val locationManager by lazy {
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val gpsLocationListener = LocationListener { location ->
        currentLocation = location
    }

    override fun onStart() {
        super.onStart()
        hideSupportActionBar()
    }

    override fun onStop() {
        super.onStop()
        showSupportActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        placeViewModel.setCurrentPlace(place)
        setupPlaceText()
        setupImageSlider()
        setClickListeners()
        observeEvents()

        setCheckInButton {
            checkLocationPermissions()
        }

        observeReviewViewModel()
        setupReviewRecyclerView()
    }

    private fun checkLocationPermissions() {
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

        locationPermissionRequestLauncher.launch(
            arrayOf(coarseLocationPermission, fineLocationPermission)
        )
    }

    private fun observeEvents() {
        placeViewModel.checkInEvents.observeFlow(viewLifecycleOwner) { event ->
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

        placeViewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
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
            binding.bCheckIn.isEnabled = !uiState.isLoading && placeViewModel.isUserAuthorized()
        }
    }

    private fun observeReviewViewModel() {
        reviewsViewModel.loadPlaceReviews(place.id.toLong())
        reviewsViewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            reviews = uiState.data?.toTypedArray()
            binding.pbLastReviews.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE

            if (uiState.isLoading){
                return@observeFlow
            }

            val lastReviews = reviewsViewModel.getLastReviews()
            if (lastReviews == null || lastReviews.isEmpty()){
                showNoReviewsUi()
                return@observeFlow
            }

            showReviewsUi()
            reviewRvAdapter.submitList(lastReviews)
        }
    }

    private fun setupReviewRecyclerView() {
        binding.rvPlacesLastReviews.adapter = reviewRvAdapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        binding.rvPlacesLastReviews.addItemDecoration(dividerItemDecoration)
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

            setCheckInButton {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                placeViewModel.checkIn(place, currentLocation)
            }
            binding.bCheckIn.performClick()
        } else {
            Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_LONG).show()
            setCheckInButton {
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
            tvPlaceDesc.text = place.shortDescription
        }
    }

    private fun setupImageSlider() {
        val imageUrls = place.images.map { it.imageUrl }
        val placeImageAdapter = PlaceImageSliderAdapter(imageUrls, requireContext())
        binding.placeImageSlider.setSliderAdapter(placeImageAdapter)
    }

    private fun setClickListeners() {
        binding.bLocatePlace.setOnClickListener {
            placeViewModel.openPlaceInMap(place)
        }

        binding.bBackPlace.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bDescription.setOnClickListener {
            navigateToPlaceDescriptionFragment()
        }

        binding.bAllReviews.setOnClickListener {
            navigateToPlaceReviewsFragment()
        }

        binding.bRatingAndReviews.setOnClickListener {
            if (reviews == null){
                binding.root.showSnackbar(UiText.DynamicText("Is loading"))
                return@setOnClickListener
            }
            if (reviews?.isEmpty() == true){
                binding.root.showSnackbar(UiText.StringResource(R.string.no_reviews))
                return@setOnClickListener
            }
            navigateToPlaceReviewsFragment()
        }
    }

    private fun navigateToReviewFragment() {
        val direction = PlaceFragmentDirections.actionPlaceFragmentToReviewFragment(place)
        findNavController().navigate(direction)
    }

    private fun navigateToPlaceReviewsFragment() {
        val direction =
            PlaceFragmentDirections.actionPlaceFragmentToPlaceReviewsFragment(place, reviews)
        findNavController().navigate(direction)
    }

    private fun navigateToPlaceDescriptionFragment(){
        val direction =
            PlaceFragmentDirections.actionPlaceFragmentToPlaceDescriptionFragment(place)
        findNavController().navigate(direction)
    }

    private fun showReviewsUi(){
        binding.rvPlacesLastReviews.visibility = View.VISIBLE
        binding.bAllReviews.visibility = View.VISIBLE

        binding.tvNoReviews.visibility = View.GONE
        binding.ivNoReviews.visibility = View.GONE
    }

    private fun showNoReviewsUi(){
        binding.rvPlacesLastReviews.visibility = View.GONE
        binding.bAllReviews.visibility = View.GONE

        binding.tvNoReviews.visibility = View.VISIBLE
        binding.ivNoReviews.visibility = View.VISIBLE
    }

}