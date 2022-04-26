package com.justnik.mosplace.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityStartBinding
import com.justnik.mosplace.helpers.prefs.UserPrefs
import com.justnik.mosplace.presentation.MainActivity


class StartActivity : AppCompatActivity() {

    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    private val userPrefs: UserPrefs by lazy { UserPrefs(this) }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(
            R.id.start_container
        ) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.login_graph)
    }

    private var showSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }

        setContentView(binding.root)
        checkAuthorization()
    }

    private fun checkAuthorization() {
        if (userPrefs.isUserAuthorized) {
            openMainActivity()
        } else {
            setStartDestination()
        }
        showSplashScreen = false
    }

    private fun setStartDestination() {
        if (userPrefs.isOnBoardingFinished) {
            navGraph.setStartDestination(R.id.loginFragment)
        } else {
            navGraph.setStartDestination(R.id.onBoardingFragment)
        }
        navController.graph = navGraph
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}