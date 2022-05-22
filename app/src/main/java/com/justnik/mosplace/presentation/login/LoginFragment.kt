package com.justnik.mosplace.presentation.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentLoginBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.setTitle
import com.justnik.mosplace.helpers.showSingle
import com.justnik.mosplace.presentation.account.ThemeDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()
    private var loginMenu: Menu? = null

    private val navController: NavController by lazy {
        findNavController()
    }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.account)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        observeViewModel()
        addTextChangeListeners()
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.account)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        loginMenu = menu
        inflater.inflate(R.menu.menu_not_authorized, menu)
        setThemeIcon()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_theme -> {
                showThemeDialog()
            }
        }
        return true
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.validationEvents.collect { event ->
                when (event) {
                    is LoginViewModel.ValidationEvent.Success -> {
                        navGraph.setStartDestination(R.id.accountFragment)
                        findNavController().graph = navGraph
                    }
                    is LoginViewModel.ValidationEvent.Error -> {
                        showAlertDialog(
                            getString(R.string.account_failed),
                            event.errorMessage.asString(requireContext())
                        )
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

        viewModel.uiState.observeFlow(viewLifecycleOwner){ uiState ->
            if (uiState.isLoading){
                binding.bLogIn.isEnabled = false
                binding.bLogIn.setIconResource(R.drawable.ic_login)
            }
            else {
                binding.bLogIn.isEnabled = true
                binding.bLogIn.icon = null
            }
        }
    }

    private fun showThemeDialog() {
        val themeDialog = ThemeDialog().apply {
            darkModeCallback = {
                setThemeIcon()
            }
        }
        themeDialog.showSingle(childFragmentManager, ThemeDialog.THEME_DIALOG_TAG)
    }

    private fun setThemeIcon() {
        val menuItem = loginMenu?.findItem(R.id.action_theme)
        when (viewModel.getDarkModeCode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                menuItem?.setIcon(R.drawable.ic_mode_auto)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                menuItem?.setIcon(R.drawable.ic_mode_light)
            }
            else -> {
                menuItem?.setIcon(R.drawable.ic_mode_dark)
            }
        }
    }


    private fun showAlertDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.got_it)) { _, _ ->

            }
            .show()

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
            Snackbar.make(binding.root, getText(R.string.should_remember), Snackbar.LENGTH_SHORT)
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