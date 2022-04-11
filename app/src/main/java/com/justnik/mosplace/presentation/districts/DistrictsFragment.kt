package com.justnik.mosplace.presentation.districts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictsBinding
import com.justnik.mosplace.presentation.adapters.district.DistrictAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DistrictsFragment : Fragment(R.layout.fragment_districts) {

    private val binding: FragmentDistrictsBinding by viewBinding()

    private val rvAdapter: DistrictAdapter by lazy {
        DistrictAdapter(requireContext())
    }

    private val viewModel: DistrictsViewModel by viewModels()

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setUpToolBar()
        observeViewModel()
        loadDistricts()
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
        viewModel.districts.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            val pbVisibility = if (it) View.VISIBLE else View.GONE
            binding.pbDistricts.visibility = pbVisibility
        }
    }

    private fun loadDistricts() {
        scope.launch {
            try {
                viewModel.loadDistricts()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
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
                    rvAdapter.submitList(viewModel.getAllDistrictsList())
                    return true
                }
                viewModel.filterDistricts(p0)
                return true
            }

        })
    }
}