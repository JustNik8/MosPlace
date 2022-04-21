package com.justnik.mosplace.presentation.review

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentReviewBinding
import com.justnik.mosplace.helpers.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {

    private val binding: FragmentReviewBinding by viewBinding()

    private val viewModel: ReviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSaveButton()
        observeViewModel()
        addTextChangeListeners()
    }

    private fun readInput() {
        val rating = binding.rbPlace.rating
        val reviewText = binding.etReview.text.toString()
        viewModel.validateInputReview(rating, reviewText)
    }

    private fun observeSaveButton() {
        binding.bSaveReview.setOnClickListener {
            readInput()
        }
    }

    private fun observeViewModel() {
        viewModel.errorRating.observeFlow(viewLifecycleOwner) {
            if (it) {
                val errorText = resources.getString(R.string.error_rating)

                Snackbar.make(
                    binding.root,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.errorReviewText.observeFlow(viewLifecycleOwner) {
            val etReview = binding.etReview

            if (it) {
                val errorText = resources.getString(R.string.error_empty_field)
                etReview.error = errorText
                etReview.requestFocus()
            } else {
                etReview.error = null
            }
        }

        viewModel.shouldCloseScreen.observeFlow(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun addTextChangeListeners() {
        binding.etReview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorReviewText()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }
}