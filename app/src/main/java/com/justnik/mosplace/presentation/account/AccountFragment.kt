package com.justnik.mosplace.presentation.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentAccountBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.setTitle
import com.justnik.mosplace.helpers.showSingle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding: FragmentAccountBinding by viewBinding()
    private val viewModel: AccountViewModel by viewModels()
    private var accountMenu: Menu? = null

    private val navController: NavController by lazy {
        findNavController()
    }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.account)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setHasOptionsMenu(true)
        setThemeIcon(AppCompatDelegate.getDefaultNightMode())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        accountMenu = menu
        inflater.inflate(R.menu.menu_authorized, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                showAlertDialog()
            }
            R.id.action_theme -> {
                showThemeDialog()
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.account)
    }

    private fun showThemeDialog() {
        val themeDialog = ThemeDialog().apply {
            darkModeCallback = { darkModeCode ->
                setThemeIcon(darkModeCode)
            }
        }
        themeDialog.showSingle(childFragmentManager, ThemeDialog.THEME_DIALOG_TAG)
    }

    private fun setThemeIcon(darkModeCode: Int) {
        val menuItem = accountMenu?.findItem(R.id.action_theme)
        when (darkModeCode) {
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

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Log out")
            .setMessage("Are you sure you want out of the account?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    private fun logout() {
        viewModel.logout()
        navGraph.setStartDestination(R.id.loginFragment)
        findNavController().graph = navGraph
    }

    private fun observeViewModel() {
        viewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            val profile = uiState.profile
            with(binding) {
                tvUsername.text = profile.name
                Glide.with(requireActivity()).load(profile.imageUrl).into(ivAvatar)
            }
        }
    }
}