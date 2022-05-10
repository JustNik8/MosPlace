package com.justnik.mosplace.presentation.review

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentReviewBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.setTitle
import com.justnik.mosplace.presentation.helpers.TextWatcherWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {

    private val binding: FragmentReviewBinding by viewBinding()

    private val viewModel: ReviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        observeViewModel()
        addTextChangeListeners()
        addRatingChangeListener()
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.review)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private fun addRatingChangeListener() {
        binding.rbPlace.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.onEvent(ReviewFormEvent.RatingChanged(rating))
        }
    }

    private fun setClickListener() {
        binding.bSaveReview.setOnClickListener {
            viewModel.onEvent(ReviewFormEvent.Submit)
        }
    }

    private fun observeViewModel() {
        viewModel.validationEvents.observeFlow(viewLifecycleOwner){ event ->
            when (event){
                is ReviewViewModel.ValidationEvent.Success -> {
                    findNavController().popBackStack()
                }
                is ReviewViewModel.ValidationEvent.Error -> {
                    Snackbar.make(binding.root, "Failed to create", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        viewModel.reviewFormState.observeFlow(viewLifecycleOwner) { state ->
            with(binding){
                tilReview.error = state.reviewError?.asString(requireContext())
                etRatingError.text = state.ratingCountError?.asString(requireContext())
            }
        }
    }

    private fun addTextChangeListeners() {
        with (binding) {
            etReview.addTextChangedListener(TextWatcherWrapper{
                viewModel.onEvent(ReviewFormEvent.ReviewChanged(etReview.text.toString()))
            })
        }
    }

}