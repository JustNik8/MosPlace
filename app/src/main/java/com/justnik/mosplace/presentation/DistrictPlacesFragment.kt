package com.justnik.mosplace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.presentation.adapters.place.PlaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DistrictPlacesFragment : Fragment() {

    private var _binding: FragmentDistrictPlacesBinding? = null
    private val binding: FragmentDistrictPlacesBinding
        get() = _binding!!

    private lateinit var district: District

    private val rvAdapter: PlaceAdapter by lazy {
        PlaceAdapter(requireContext())
    }

    private val viewModel: MosViewModel by lazy {
        ViewModelProvider(this)[MosViewModel::class.java]
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    var onPlaceClickListener: ((Place) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        district = arguments?.getParcelable(KEY_DISTRICT)
            ?: throw RuntimeException("Param district is null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistrictPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        launchPlaces()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView(){
        binding.rvPlaces.adapter = rvAdapter

        rvAdapter.onPlaceClickListener = {
            onPlaceClickListener?.invoke(it)
        }
    }

    private fun launchPlaces(){
        scope.launch {
            val places = viewModel.loadPlacesByDistrictId(district.id)
            rvAdapter.submitList(places)
        }
    }

    companion object {
        fun newInstance(district: District): DistrictPlacesFragment {
            return DistrictPlacesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DISTRICT, district)
                }
            }
        }

        private const val KEY_DISTRICT = "district"
    }

}