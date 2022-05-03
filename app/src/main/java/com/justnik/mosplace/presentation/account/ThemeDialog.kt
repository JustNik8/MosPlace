package com.justnik.mosplace.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.SettingsPrefs
import com.justnik.mosplace.databinding.DialogThemeBinding

class ThemeDialog : BottomSheetDialogFragment() {
    private val binding: DialogThemeBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val settingsPrefs by lazy { SettingsPrefs(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Mosplace_BottomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        with (binding){
            bCancel.setOnClickListener { dismiss() }

            bLightMode.setOnClickListener {
                setDefaultNightMode(MODE_NIGHT_NO)
                settingsPrefs.darkModeCode = MODE_NIGHT_NO
            }
            bDarkMode.setOnClickListener {
                setDefaultNightMode(MODE_NIGHT_YES)
                settingsPrefs.darkModeCode = MODE_NIGHT_YES
            }
            bAutoMode.setOnClickListener {
                setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                settingsPrefs.darkModeCode = MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    }

    companion object {
        const val THEME_DIALOG_TAG = "theme"
    }
}