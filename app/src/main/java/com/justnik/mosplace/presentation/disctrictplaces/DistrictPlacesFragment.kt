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
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.presentation.adapters.place.PlaceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupToolBar()
        observeViewModel()
        viewModel.loadPlacesByDistrictId(district.id)
        setClickListener()
    }

    private fun setClickListener() {
        binding.bTryAgain.setOnClickListener {
            viewModel.loadPlacesByDistrictId(district.id)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observeFlow(viewLifecycleOwner){ uiState ->
            val isLoading = uiState.isLoading
            binding.pbDistrictPlaces.visibility = if (isLoading) View.VISIBLE else View.GONE

            when (uiState.error){
                is UiState.Error.NetworkError -> {
                    val errorText = requireActivity().getString(uiState.error.errorResId)
                    showErrorUi(errorText)
                }
                else -> {
                    rvAdapter.submitList(uiState.places)
                    showMainUi()
                }
            }
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

    private fun setupRecyclerView() {
        binding.rvPlaces.adapter = rvAdapter

        rvAdapter.onPlaceClickListener = {
            findNavController().navigate(
                DistrictPlacesFragmentDirections.actionDistrictPlacesFragmentToPlace(it)
            )
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

    private fun showMainUi(){
        //Hide error views
        binding.ivNoInternet.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.bTryAgain.visibility = View.GONE
        //Show district list
        binding.rvPlaces.visibility = View.VISIBLE
    }

    private fun showErrorUi(errorText: String) {
        //Show error views
        binding.ivNoInternet.visibility = View.VISIBLE
        binding.tvError.visibility = View.VISIBLE
        binding.bTryAgain.visibility = View.VISIBLE
        binding.tvError.text = errorText
        //Hide district list
        binding.rvPlaces.visibility = View.GONE
    }
}
