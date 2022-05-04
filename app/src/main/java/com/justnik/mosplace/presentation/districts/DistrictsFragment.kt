package com.justnik.mosplace.presentation.districts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictsBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistrictsFragment : Fragment(R.layout.fragment_districts) {

    private val binding: FragmentDistrictsBinding by viewBinding()

    private val rvAdapter: DistrictAdapter by lazy {
        DistrictAdapter(requireContext())
    }

    private var districtsMenu: Menu? = null

    private val viewModel: DistrictsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        observeViewModel()
        setClickListener()
        setHasOptionsMenu(true)
        setTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        districtsMenu = menu
        inflater.inflate(R.menu.menu_district, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_search -> {
                val searchView = item.actionView as SearchView
                setupSearchView(searchView)
            }
        }
        return true
    }

    private fun setupSearchView(searchView: SearchView){
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

    private fun setClickListener() {
        binding.bTryAgain.setOnClickListener {
            viewModel.loadDistricts()
        }
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
            districtsMenu?.findItem(R.id.action_search)?.collapseActionView()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observeFlow(viewLifecycleOwner){ uiState ->
            val isLoading = uiState.isLoading
            binding.pbDistricts.visibility = if (isLoading) View.VISIBLE else View.GONE

            when (uiState.error){
                is UiState.Error.NetworkError -> {
                    val errorText = requireActivity().getString(uiState.error.errorResId)
                    showErrorUi(errorText)
                }
                else -> {
                    rvAdapter.submitList(uiState.districts)
                    showMainUi()
                }
            }
        }
    }


    private fun setTitle() {
        (requireActivity() as MainActivity).setTitle(R.string.districts)
    }

    private fun showMainUi(){
        //Hide error views
        binding.ivNoInternet.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.bTryAgain.visibility = View.GONE
        //Show district list
        binding.rvDistricts.visibility = View.VISIBLE
    }

    private fun showErrorUi(errorText: String) {
        //Show error views
        binding.ivNoInternet.visibility = View.VISIBLE
        binding.tvError.visibility = View.VISIBLE
        binding.bTryAgain.visibility = View.VISIBLE
        binding.tvError.text = errorText
        //Hide district list
        binding.rvDistricts.visibility = View.GONE
    }
}

