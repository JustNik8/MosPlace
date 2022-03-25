package com.justnik.mosplace.presentation.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.presentation.adapters.placeimages.PlaceImageSliderAdapter

class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var place: Place

    private val viewModel: PlaceViewModel by lazy {
        ViewModelProvider(this)[PlaceViewModel::class.java]
    }

    var onReviewButtonClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        place = arguments?.getParcelable(KEY_PLACE)
            ?: throw RuntimeException("argument place is null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.place = place

        setUpImageSlider()
        setClickListeners()
    }

        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpImageSlider(){
        val imageUrls = place.images.map { it.imageUrl }
        val placeImageAdapter = PlaceImageSliderAdapter(imageUrls, requireContext())
        binding.placeImageSlider.setSliderAdapter(placeImageAdapter)
    }

    private fun setClickListeners() {
        binding.bLocatePlace.setOnClickListener {
            viewModel.openPlaceInMap(place)
        }

        binding.bReview.setOnClickListener {
            onReviewButtonClickListener?.invoke()
        }
    }
    companion object{
        fun newInstance(place: Place): PlaceFragment {
            return PlaceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_PLACE, place)
                }
            }
        }

        private const val KEY_PLACE = "place"
    }
}