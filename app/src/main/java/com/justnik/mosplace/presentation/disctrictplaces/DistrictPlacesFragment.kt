package com.justnik.mosplace.presentation.disctrictplaces

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

    //todo It should be refactored
    private val uniquePlace by lazy { parsePlaceType(PlaceTypes.UNIQUE_PLACE, requireContext()) }
    private val restaurant by lazy { parsePlaceType(PlaceTypes.RESTAURANT, requireContext()) }
    private val park by lazy { parsePlaceType(PlaceTypes.PARK, requireContext()) }

    //Items that will be shown in alert dialog
    private val multiItems by lazy { arrayOf(uniquePlace, restaurant, park) }

    //Array that contains if item is checked
    private val checkedItems by lazy { BooleanArray(multiItems.size) { true } }
    //todo It should be refactored

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
                    showMaterialDialog()
                    true
                }
                else -> false
            }
        }

    }

    private fun showMaterialDialog() {

        //List which contains selected items
        val selectedItems = mutableListOf<String>()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.places))
            .setNeutralButton(requireContext().getString(R.string.select_all)) { _, _ ->
                selectedItems.addAll(multiItems)
                viewModel.filterPlacesByType(selectedItems)
            }
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(requireContext().getString(R.string.apply)) { _, _ ->
                for (i in checkedItems.indices) {
                    if (checkedItems[i]) {
                        selectedItems.add(multiItems[i])
                    }
                }
                viewModel.filterPlacesByType(selectedItems)
            }
            .setMultiChoiceItems(multiItems, checkedItems) { _, which, checked ->
                checkedItems[which] = checked
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
