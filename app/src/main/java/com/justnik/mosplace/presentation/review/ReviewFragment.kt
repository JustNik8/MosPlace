package com.justnik.mosplace.presentation.review

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ReviewViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUpBottomBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSaveButton()
        observeViewModel()
        addTextChangeListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        setUpBottomBar()
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
        viewModel.errorRating.observe(viewLifecycleOwner) {
            if (it) {
                val errorText = resources.getString(R.string.error_rating)

                Snackbar.make(
                    binding.root,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.errorReviewText.observe(viewLifecycleOwner) {
            val etReview = binding.etReview

            if (it) {
                val errorText = resources.getString(R.string.error_empty_field)
                etReview.error = errorText
                etReview.requestFocus()
            } else {
                etReview.error = null
            }
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
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

    private fun setUpBottomBar() {
        val bottomBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation)

        bottomBar.visibility = if (bottomBar.isVisible) View.GONE else View.VISIBLE
    }


    companion object {
        @JvmStatic
        fun newInstance(): ReviewFragment {
            return ReviewFragment()
        }
    }
}