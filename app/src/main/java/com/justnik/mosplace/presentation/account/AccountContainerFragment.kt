package com.justnik.mosplace.presentation.account

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.databinding.FragmentAccountContainerBinding
import com.justnik.mosplace.helpers.showSingle
import com.justnik.mosplace.presentation.MainActivity
import com.justnik.mosplace.presentation.start.StartActivity

class AccountContainerFragment : Fragment(R.layout.fragment_account_container) {

    private val binding: FragmentAccountContainerBinding by viewBinding()
    private val userPrefs by lazy { UserPrefs(requireContext()) }

    private val navHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.account_container) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.account)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStartDestination()
        setupToolbar()
        setThemeIcon(AppCompatDelegate.getDefaultNightMode())
        setupThemeButtonClickListener()
        setUpLogoutButtonClickListener()

    }

    private fun setupToolbar() {
        val isAuthorized = userPrefs.jwtAccessToken != null
        val toolbar = binding.accountToolbar
        val menuRes = if (isAuthorized){
            R.menu.menu_authorized
        } else {
            R.menu.menu_not_authorized
        }
        toolbar.inflateMenu(menuRes)
    }

    private fun setUpLogoutButtonClickListener() {
        val menuItem: MenuItem? = binding.accountToolbar.menu.findItem(R.id.action_logout)

        menuItem?.setOnMenuItemClickListener {
            showAlertDialog()
            true
        }
    }

    private fun setupThemeButtonClickListener() {
        //get menu item
        val menuItem = binding.accountToolbar.menu.findItem(R.id.action_theme)

        menuItem.setOnMenuItemClickListener {
            val themeDialog = ThemeDialog().apply {
                darkModeCallback = { darkModeCode ->
                    setThemeIcon(darkModeCode)
                }
            }
            themeDialog.showSingle(childFragmentManager, ThemeDialog.THEME_DIALOG_TAG)
            true
        }
    }

    private fun setStartDestination() {
        if (userPrefs.jwtAccessToken != null) {
            navGraph.setStartDestination(R.id.AccountFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.graph = navGraph
    }

    private fun setThemeIcon(darkModeCode: Int) {
        val menuItem = binding.accountToolbar.menu.findItem(R.id.action_theme)
        when (darkModeCode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                menuItem.setIcon(R.drawable.ic_mode_auto)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                menuItem.setIcon(R.drawable.ic_mode_light)
            }
            else -> {
                menuItem.setIcon(R.drawable.ic_mode_dark)
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
        userPrefs.jwtAccessToken = null
        userPrefs.jwtRefreshToken = null

        val parentActivity = requireActivity()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        parentActivity.startActivity(intent)
        parentActivity.finish()
    }

}