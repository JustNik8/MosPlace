package com.justnik.mosplace.presentation.disctrictplaces

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.parsePlaceType
import com.justnik.mosplace.data.prefs.PlaceTypePrefs
import com.justnik.mosplace.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

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
        setHasOptionsMenu(true)
        observeViewModel()
        viewModel.loadPlacesByDistrictId(district.id)
        setClickListener()
        setTitle()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_district_places, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_list -> {
                showAlertDialog()

            }
            else -> findNavController().popBackStack()
        }
        return true
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


    private fun setupRecyclerView() {
        binding.rvPlaces.adapter = rvAdapter

        rvAdapter.onPlaceClickListener = {
            findNavController().navigate(
                DistrictPlacesFragmentDirections.actionDistrictPlacesFragmentToPlace(it)
            )
        }
    }

    private fun setTitle() = (requireActivity() as MainActivity).setTitle(R.string.places)

    private fun showAlertDialog() {
        val placeTypePrefs = PlaceTypePrefs(requireContext())

        //Text for buttons and title
        val negativeButtonText = resources.getString(R.string.cancel)
        val titleText = resources.getString(R.string.places)
        val positiveButtonText = resources.getString(R.string.apply)

        val prefsTypes = placeTypePrefs.selectedPrefsTypes

        //Items that will be shown in alert dialog Only for showing in alert dialog
        val availableTypes = prefsTypes.map {
            parsePlaceType(it.typeName, requireContext())
        }.toTypedArray()

        //Array to show selected items. Only for showing in alert dialog
        val selectedTypes = prefsTypes.map { it.selected }.toBooleanArray()

        //Build alert dialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleText)
            .setNegativeButton(negativeButtonText) { _, _ ->
            }
            .setPositiveButton(positiveButtonText) { _, _ ->
                prefsTypes.onEach {
                    placeTypePrefs.setTypeSelection(it)
                }

                val selectedItems = prefsTypes.filter { it.selected }.map { it.typeName }
                viewModel.filterPlacesByType(selectedItems)
            }
            .setMultiChoiceItems(availableTypes, selectedTypes) { _, which, checked ->
                prefsTypes[which].selected = checked
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
