package com.justnik.mosplace.presentation.districts

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictsBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.presentation.adapters.district.DistrictAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistrictsFragment : Fragment(R.layout.fragment_districts) {

    private val binding: FragmentDistrictsBinding by viewBinding()

    private val rvAdapter: DistrictAdapter by lazy {
        DistrictAdapter(requireContext())
    }

    private val viewModel: DistrictsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setUpToolBar()
        observeViewModel()
    }

    private fun setUpRecyclerView() {
        binding.rvDistricts.adapter = rvAdapter

        rvAdapter.onDistrictClickListener = {
            findNavController().navigate(
                DistrictsFragmentDirections
                    .actionDistrictsFragmentToDistrictPlacesFragment(
                        it
                    )
            )
            val menuItem = binding.toolbarDistricts.menu.findItem(R.id.action_search)
            menuItem.collapseActionView()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observeFlow(viewLifecycleOwner){ uiState ->
            val isLoading = uiState.isLoading
            binding.pbDistricts.visibility = if (isLoading) View.VISIBLE else View.GONE

            when (uiState.error){
                is UiState.Error.NetworkError -> {
                    val errorText = requireActivity().getString(uiState.error.errorResId)
                    Snackbar.make(binding.root, errorText, Snackbar.LENGTH_SHORT).show()
                }
                else -> {rvAdapter.submitList(uiState.districts)}
            }
        }
    }

    private fun setUpToolBar() {
        val menuItem = binding.toolbarDistricts.menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = requireContext().resources.getString(R.string.hint_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                if (p0 == "") {
                    viewModel.filterDistricts(null)
                    return true
                }
                viewModel.filterDistricts(p0)
                return true
            }
        })
    }
}

