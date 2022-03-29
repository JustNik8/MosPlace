package com.justnik.mosplace.presentation.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.presentation.adapters.placeimages.PlaceImageSliderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceFragment : Fragment() {

    private val args by navArgs<PlaceFragmentArgs>()

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!

    private val place by lazy {
        args.place
    }

    private val viewModel: PlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPlaceText()
        setUpImageSlider()
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpPlaceText() {
        with(binding) {
            tvPlaceTitle.text = place.title
            tvPlaceDesc.text = place.fullDescription
        }
    }

    private fun setUpImageSlider() {
        val imageUrls = place.images.map { it.imageUrl }
        val placeImageAdapter = PlaceImageSliderAdapter(imageUrls, requireContext())
        binding.placeImageSlider.setSliderAdapter(placeImageAdapter)
    }

    private fun setClickListeners() {
        binding.bLocatePlace.setOnClickListener {
            viewModel.openPlaceInMap(place)
        }

        binding.bReview.setOnClickListener {
            findNavController().navigate(
                PlaceFragmentDirections.actionPlaceFragmentToReviewFragment()
            )
        }
    }
}