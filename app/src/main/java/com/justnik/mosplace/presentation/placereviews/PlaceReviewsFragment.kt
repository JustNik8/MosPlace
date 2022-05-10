package com.justnik.mosplace.presentation.placereviews

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentPlaceReviewsBinding
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.helpers.setTitle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceReviewsFragment : Fragment(R.layout.fragment_place_reviews) {
    private val binding: FragmentPlaceReviewsBinding by viewBinding()

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

        rvAdapter.submitList(createReviews())
    }

    private fun createReviews() = listOf(
        Review(1, PROFILE_IMAGE_URL, "Alex", 4, "10.05.2022", "Great"),
        Review(2, PROFILE_IMAGE_URL, "Vasilisa Andreevna", 5, "04.07.2022", "I like this place!"),
        Review(3, PROFILE_IMAGE_URL, "Egorova Sofia", 3, "02.06.2022", "Nothing special"),
        Review(4, PROFILE_IMAGE_URL, "Markov Egor", 1, "16.12.2021", "That's such an awful place. I am going to never visit this"),
        Review(5, PROFILE_IMAGE_URL, "Ivanov Artyom", 2, "28.03.2022", "Bad"),
        Review(6, PROFILE_IMAGE_URL, "Sotnikov Andrey", 5, "19.01.2022", "The best place I have visited"),
        Review(7, PROFILE_IMAGE_URL, "Zubova Angelina", 5, "18.03.2022", "Amazing!"),
        Review(7, PROFILE_IMAGE_URL, "Serova Nikol'", 4, "07.02.2022", "Good, but has some disadvantages")
    )

    companion object {
        private const val PROFILE_IMAGE_URL = "https://www.fotoget.net/wp-content/uploads/2016/02/man_north-1650x1100.jpg"
    }
}