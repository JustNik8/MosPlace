package com.justnik.mosplace.presentation.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentRegistrationBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.setTitle
import com.justnik.mosplace.presentation.helpers.TextWatcherWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val binding: FragmentRegistrationBinding by viewBinding()
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListener()
        observeViewModel()
        addTextChangeListeners()
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.registration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private fun setClickListener() {
        binding.bCreateAccount.setOnClickListener {
            viewModel.onEvent(RegistrationFormEvent.Submit)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.validationEvents.collect { event ->
                when (event) {
                    is RegistrationViewModel.ValidationEvent.Success -> {
                        showAlertDialog(
                            getString(R.string.account_created),
                            event.successMessage.asString(requireContext()),
                            successful = true
                        )
                    }
                    is RegistrationViewModel.ValidationEvent.Error -> {
                        showAlertDialog(
                            getString(R.string.account_failed),
                            event.errorMessage.asString(requireContext())
                        )
                    }
                }
            }
        }

        viewModel.registrationFormState.observeFlow(viewLifecycleOwner) {
            with(binding) {
                tilEmail.error = it.emailError?.asString(requireContext())
                tilUsername.error = it.usernameError?.asString(requireContext())
                tilPassword.error = it.passwordError?.asString(requireContext())
                tilRepeatedPassword.error = it.repeatedPasswordError?.asString(requireContext())
            }
        }

        viewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            if (uiState.isLoading){
                binding.bCreateAccount.isEnabled = false
                binding.bCreateAccount.setIconResource(R.drawable.ic_person_add)
            }
            else {
                binding.bCreateAccount.isEnabled = true
                binding.bCreateAccount.icon = null
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, successful: Boolean = false) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.got_it)) { _, _ ->
                if (successful) {
                    findNavController().popBackStack()
                }
            }
            .show()

    }

    private fun addTextChangeListeners() {
        with(binding) {
            etEmail.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(etEmail.text.toString()))
            })

            etUsername.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(RegistrationFormEvent.UsernameChanged(etUsername.text.toString()))
            })

            etPassword.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(RegistrationFormEvent.PasswordChanged(etPassword.text.toString()))
            })
            etRepeatedPassword.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(
                    RegistrationFormEvent.RepeatedPasswordChanged(
                        etRepeatedPassword.text.toString()
                    )
                )
            })
        }
    }

}


