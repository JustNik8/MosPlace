package com.justnik.mosplace.presentation.placereviews

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceReviewsBinding
import com.justnik.mosplace.helpers.setTitle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceReviewsFragment : Fragment(R.layout.fragment_place_reviews) {

    private val binding: FragmentPlaceReviewsBinding by viewBinding()

    private val args by navArgs<PlaceReviewsFragmentArgs>()
    private val place by lazy { args.place }
    private val reviews by lazy { args.placeReviews }

    private val rvAdapter by lazy {
        PlaceReviewsAdapter(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.all_reviews)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        binding.rvReviews.adapter = rvAdapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        binding.rvReviews.addItemDecoration(dividerItemDecoration)

        rvAdapter.submitList(reviews?.toList())
    }

}