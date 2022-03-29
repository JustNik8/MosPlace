package com.justnik.mosplace.presentation.disctrictplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.presentation.adapters.place.PlaceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DistrictPlacesFragment : Fragment() {

    private val args by navArgs<DistrictPlacesFragmentArgs>()

    private var _binding: FragmentDistrictPlacesBinding? = null
    private val binding: FragmentDistrictPlacesBinding
        get() = _binding!!

    private val district by lazy {
        args.district
    }

    private val rvAdapter: PlaceAdapter by lazy {
        PlaceAdapter(requireContext())
    }

    private val viewModel: DistrictPlacesViewModel by viewModels()

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistrictPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        loadPlaces()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
