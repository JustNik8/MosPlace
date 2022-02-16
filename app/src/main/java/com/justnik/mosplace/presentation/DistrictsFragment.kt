package com.justnik.mosplace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.databinding.FragmentDistrictsBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.presentation.adapters.DistrictAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DistrictsFragment : Fragment() {

    private var _binding: FragmentDistrictsBinding? = null
    private val binding: FragmentDistrictsBinding
        get() = _binding!!

    private val rvAdapter: DistrictAdapter by lazy {
        DistrictAdapter(requireContext())
    }

    private val viewModel: MosViewModel by lazy {
        ViewModelProvider(this)[MosViewModel::class.java]
    }

    private val scope = CoroutineScope(Dispatchers.Main)

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

    private fun loadDistricts() {
        scope.launch {
            val districts = viewModel.loadDistricts()
            rvAdapter.submitList(districts)
        }
    }

    companion object {
        fun newInstance(): DistrictsFragment {
            return DistrictsFragment()
        }
    }
}