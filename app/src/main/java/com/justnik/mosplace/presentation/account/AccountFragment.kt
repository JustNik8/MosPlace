package com.justnik.mosplace.presentation.account

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentAccountBinding
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.showSingle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding: FragmentAccountBinding by viewBinding()
    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setThemeIcon(AppCompatDelegate.getDefaultNightMode())
        setupThemeButtonClickListener()
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

    private fun observeViewModel() {
        viewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            val profile = uiState.profile
            with(binding) {
                tvUsername.text = profile.name
                Glide.with(requireActivity()).load(profile.imageUrl).into(ivAvatar)
            }
        }
    }

    private fun setThemeIcon(darkModeCode: Int){
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

}