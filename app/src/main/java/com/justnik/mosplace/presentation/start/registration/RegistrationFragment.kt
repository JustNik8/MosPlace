package com.justnik.mosplace.presentation.start.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentRegistrationBinding
import com.justnik.mosplace.helpers.observeFlow
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
                        showAlertDialog("An account created", event.successMessage)
                    }
                    is RegistrationViewModel.ValidationEvent.Error -> {
                        showAlertDialog("Failed to create an account", event.errorMessage)
                    }
                }
            }
        }

        viewModel.registrationFormState.observeFlow(viewLifecycleOwner) {
            with(binding) {
                //Log.d("RRR", it.toString())
                tilEmail.error = it.emailError
                tilUsername.error = it.usernameError
                tilPassword.error = it.passwordError
                tilRepeatedPassword.error = it.repeatedPasswordError
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, successful: Boolean = false) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Got it") { _, _ ->
                if (successful){
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
                viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(
                    etRepeatedPassword.text.toString()
                ))
            })
        }
    }
}

private class TextWatcherWrapper(private val block: () -> Unit) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        block()
    }

    override fun afterTextChanged(p0: Editable?) {}
}

