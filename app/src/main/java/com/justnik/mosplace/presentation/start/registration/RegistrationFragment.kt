package com.justnik.mosplace.presentation.start.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentRegistrationBinding
import com.justnik.mosplace.helpers.observeFlow
import dagger.hilt.android.AndroidEntryPoint

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
            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val pass = binding.etPass.text.toString()
            val confirmPass = binding.etConfirmPass.text.toString()
            viewModel.validateInput(email, username, pass, confirmPass)
        }
    }

    private fun observeViewModel() {
        viewModel.emailErrorState.observeFlow(viewLifecycleOwner) {
            binding.tilEmail.error = it
        }

        viewModel.usernameErrorState.observeFlow(viewLifecycleOwner) {
            binding.tilUsername.error = it
        }

        viewModel.passErrorState.observeFlow(viewLifecycleOwner) {
            binding.tilPass.error = it
        }

        viewModel.confirmPassErrorState.observeFlow(viewLifecycleOwner) {
            binding.tilConfirmPass.error = it
        }

        viewModel.passwordsAreSame.observeFlow(viewLifecycleOwner) {
            binding.tilPass.error = it
            binding.tilConfirmPass.error = it
        }
    }

    private fun addTextChangeListeners() {
        binding.etEmail.addTextChangedListener(TextWatcherWrapper { viewModel.resetEmailError() })
        binding.etUsername.addTextChangedListener(TextWatcherWrapper { viewModel.resetUsernameError() })

        binding.etPass.addTextChangedListener(TextWatcherWrapper{
            viewModel.resetPassError()
            viewModel.resetPasswordsNotSameError()
        })

        binding.etConfirmPass.addTextChangedListener(TextWatcherWrapper{
            viewModel.resetConfirmPassError()
            viewModel.resetPasswordsNotSameError()
        })
    }
}

private class TextWatcherWrapper(private val block: () -> Unit) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        block()
    }

    override fun afterTextChanged(p0: Editable?) {}
}

