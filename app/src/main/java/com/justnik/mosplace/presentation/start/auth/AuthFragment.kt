package com.justnik.mosplace.presentation.start.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentAuthBinding

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding: FragmentAuthBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bLogin.setOnClickListener {
            Toast.makeText(requireContext(), "Login Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.bRegistration.setOnClickListener {
            Toast.makeText(requireContext(), "Registration Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}