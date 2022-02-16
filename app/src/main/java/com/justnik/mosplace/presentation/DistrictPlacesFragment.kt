package com.justnik.mosplace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.justnik.mosplace.databinding.FragmentDistrictPlacesBinding
import com.justnik.mosplace.domain.entities.District

class DistrictPlacesFragment : Fragment() {

    private var _binding: FragmentDistrictPlacesBinding? = null
    private val binding: FragmentDistrictPlacesBinding
        get() = _binding!!

    private lateinit var district: District

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
        binding.district = district
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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