package com.justnik.mosplace.presentation.place

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceBinding
import com.justnik.mosplace.databinding.FragmentPlaceDescriptionBinding
import com.justnik.mosplace.helpers.setTitle

class PlaceDescriptionFragment : Fragment(R.layout.fragment_place_description) {

    private val binding: FragmentPlaceDescriptionBinding by viewBinding()
    private val args by navArgs<PlaceDescriptionFragmentArgs>()

    private val place by lazy { args.place }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        setDescriptionText()
    }

    private fun setDescriptionText() {
        binding.description.text = place.fullDescription
    }

    override fun onResume() {
        super.onResume()
        setTitle(R.string.description)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

}