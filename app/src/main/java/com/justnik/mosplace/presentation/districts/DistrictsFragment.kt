package com.justnik.mosplace.presentation.districts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentDistrictsBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.presentation.adapters.district.DistrictAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class DistrictsFragment : Fragment() {

    private var _binding: FragmentDistrictsBinding? = null
    private val binding: FragmentDistrictsBinding
        get() = _binding!!

    private val rvAdapter: DistrictAdapter by lazy {
        DistrictAdapter(requireContext())
    }

    private val viewModel: DistrictsViewModel by lazy {
        ViewModelProvider(this)[DistrictsViewModel::class.java]
    }

    private val scope = CoroutineScope(Dispatchers.Main)
    private var allDistricts = listOf<District>()

    var onDistrictCLickListener: ((District) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistrictsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setUpToolBar()
        observeViewModel()
        loadDistricts()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView() {
        binding.rvDistricts.adapter = rvAdapter

        rvAdapter.onDistrictClickListener = {
            onDistrictCLickListener?.invoke(it)
        }
    }

    private fun observeViewModel(){
        viewModel.districts.observe(viewLifecycleOwner){
            rvAdapter.submitList(it)
        }
    }

    private fun loadDistricts() {
        scope.launch {
            try {
                allDistricts = viewModel.loadDistricts()
                rvAdapter.submitList(allDistricts)
            } catch (e: UnknownHostException){
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpToolBar(){
        val menuItem = binding.toolbarDistricts.menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = requireContext().resources.getString(R.string.hint_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                if(p0 == ""){
                    rvAdapter.submitList(allDistricts)
                    return true
                }
                viewModel.filterDistricts(allDistricts, p0)
                return true
            }

        })
    }

    companion object {
        fun newInstance(): DistrictsFragment {
            return DistrictsFragment()
        }
    }
}