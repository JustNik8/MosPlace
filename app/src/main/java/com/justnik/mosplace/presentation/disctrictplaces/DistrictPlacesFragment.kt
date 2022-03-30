package com.justnik.mosplace.presentation.disctrictplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.presentation.adapters.place.PlaceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DistrictPlacesFragment : Fragment(R.layout.fragment_district_places) {

    private val args by navArgs<DistrictPlacesFragmentArgs>()

    private val binding: FragmentDistrictPlacesBinding by viewBinding()

    private val district by lazy {
        args.district
    }

    private val rvAdapter: PlaceAdapter by lazy {
        PlaceAdapter(requireContext())
    }

    private val viewModel: DistrictPlacesViewModel by viewModels()

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        loadPlaces()
    }

    private fun setUpRecyclerView() {
        binding.rvPlaces.adapter = rvAdapter

        rvAdapter.onPlaceClickListener = {
            findNavController().navigate(
                DistrictPlacesFragmentDirections.actionDistrictPlacesFragmentToPlaceFragment(
                    it
                )
            )
        }
    }

    private fun loadPlaces() {
        scope.launch {
            val places = viewModel.loadPlacesByDistrictId(district.id)
            rvAdapter.submitList(places)
        }
    }
}
