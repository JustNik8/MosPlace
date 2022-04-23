package com.justnik.mosplace.presentation

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityMainBinding
import com.justnik.mosplace.presentation.onboarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(
            R.id.main_container
        ) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomNav()

        val open = intent.getBooleanExtra("open", true)
        if (open) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setupBottomNav() {
        with(binding) {
            //setup bottom nav with navController
            mainBottomNav.setupWithNavController(navController)

            //set on item reselected listener
            mainBottomNav.setOnItemReselectedListener {
                if (mainBottomNav.selectedItemId == it.itemId) {
                    when (it.itemId) {
                        R.id.main -> refresh(R.id.districtsFragment)
                        R.id.map -> refresh(R.id.mapFragment)
                        R.id.account -> refresh(R.id.accountFragment)
                    }
                }
            }
        }
    }

    private fun refresh(@IdRes destinationId: Int) {
        findNavController(R.id.main_container)
            .popBackStack(destinationId, false)
    }
}