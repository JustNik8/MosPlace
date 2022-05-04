package com.justnik.mosplace.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.SettingsPrefs
import com.justnik.mosplace.databinding.ActivityStartBinding
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    private val userPrefs: UserPrefs by lazy { UserPrefs(this) }
    private val settingsPrefs by lazy { SettingsPrefs(this) }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(
            R.id.start_container
        ) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.onboarding_graph)
    }

    val onBoardingFinishedCallback = {
        openMainActivity()
    }

    private var showSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDarkMode()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }

        setContentView(binding.root)
        checkOnBoardingFinished()
    }

    private fun checkOnBoardingFinished() {
        if (userPrefs.isOnBoardingFinished){
            openMainActivity()
        }
        showSplashScreen = false
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(settingsPrefs.darkModeCode)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}