package com.justnik.mosplace.presentation.place

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.helpers.hideSupportActionBar
import com.justnik.mosplace.helpers.showSupportActionBar
import com.justnik.mosplace.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceFragment : Fragment(R.layout.fragment_place) {

    private val args: PlaceFragmentArgs by navArgs()

    private val binding: FragmentPlaceBinding by viewBinding()

    private val place by lazy {
        args.place
    }

    private val viewModel: PlaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSupportActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPlaceText()
        setupImageSlider()
        setClickListeners()
        setCheckInButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        showSupportActionBar()
    }

    private fun setCheckInButton() {
        binding.bCheckIn.isEnabled = viewModel.isUserAuthorized()
        binding.bCheckIn.setOnClickListener {
            findNavController().navigate(PlaceFragmentDirections.actionPlaceFragmentToReviewFragment())
        }
    }

    private fun setupPlaceText() {
        with(binding) {
            tvPlaceTitle.text = place.title
            tvPlaceDesc.text = place.fullDescription
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

        binding.bBackPlace.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}