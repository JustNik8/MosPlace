package com.justnik.mosplace.presentation.disctrictplaces

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.PlaceTypes
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.domain.parsePlaceType
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
        setupRecyclerView()
        loadPlaces()
        setupToolBar()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.places.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }
    }

    private fun setupToolBar() {
        val toolbar = binding.toolbarPlaces

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_filter_list -> {
                    showAlertDialog()
                    true
                }
                else -> false
            }
        }

    }

    private fun showAlertDialog() {
        //Text for buttons and title
        val negativeButtonText = resources.getString(R.string.cancel)
        val titleText = resources.getString(R.string.places)
        val positiveButtonText = resources.getString(R.string.apply)

        //Items that will be shown in alert dialog
        val availableTypes = viewModel.getAvailableTypes()

        //Array that contains if item is selected
        val selectedTypes = viewModel.getSelectedTypes()
        //List which contains selected items
        val selectedItems = mutableListOf<String>()

        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_key_types), Context.MODE_PRIVATE
        )

        //Build alert dialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleText)
            .setNegativeButton(negativeButtonText) { _, _ ->
            }
            .setPositiveButton(positiveButtonText) { _, _ ->
                for (i in selectedTypes.indices) {
                    if (selectedTypes[i]) {
                        selectedItems.add(availableTypes[i])
                    }
                    with(sharedPref.edit()) {
                        putBoolean(availableTypes[i], selectedTypes[i])
                        apply()
                    }
                }
                viewModel.filterPlacesByType(selectedItems)
            }
            .setMultiChoiceItems(availableTypes, selectedTypes) { _, which, checked ->
                selectedTypes[which] = checked
            }
            .show()
    }

    private fun setupRecyclerView() {
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
            viewModel.loadPlacesByDistrictId(district.id)
        }
    }
}
