package com.justnik.mosplace.presentation.start.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentLoginBinding
import com.justnik.mosplace.helpers.observeFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        observeViewModel()
        addTextChangeListeners()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.validationEvents.collect { event ->
                when (event) {
                    is LoginViewModel.ValidationEvent.Success -> {
                        Toast.makeText(
                            requireContext(),
                            event.successMessage.asString(requireContext()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is LoginViewModel.ValidationEvent.Error -> {
                        Toast.makeText(
                            requireContext(),
                            event.errorMessage.asString(requireContext()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.loginFormState.observeFlow(viewLifecycleOwner) { state ->
            with(binding) {
                tilEmail.error = state.emailError?.asString(requireContext())
                tilPassword.error = state.passwordError?.asString(requireContext())
            }
        }
    }

    private fun addTextChangeListeners() {
        with(binding) {
            etEmail.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(LoginFormEvent.EmailChanged(etEmail.text.toString()))
            })

            etPassword.addTextChangedListener(TextWatcherWrapper {
                viewModel.onEvent(LoginFormEvent.PasswordChanged(etPassword.text.toString()))
            })
        }
    }

    private fun setClickListeners() {
        binding.bRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.bForgotPass.setOnClickListener {
            Snackbar.make(binding.root, getText(R.string.forgot_password), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.bLogIn.setOnClickListener {
            viewModel.onEvent(LoginFormEvent.Submit)
        }
    }

    private class TextWatcherWrapper(private val block: () -> Unit) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            block()
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

}