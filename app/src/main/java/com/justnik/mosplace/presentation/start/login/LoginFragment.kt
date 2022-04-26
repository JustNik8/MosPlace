package com.justnik.mosplace.presentation.start.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.bRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.bForgotPass.setOnClickListener {
            Snackbar.make(binding.root, getText(R.string.forgot_password), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

}