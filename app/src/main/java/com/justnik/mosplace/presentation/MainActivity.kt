package com.justnik.mosplace.presentation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main),
    NavController.OnDestinationChangedListener {

    private val binding: ActivityMainBinding by viewBinding()
    private val userPrefs by lazy { UserPrefs(this) }

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
        setSupportActionBar(binding.mainToolbar)
        navController.addOnDestinationChangedListener(this)
        setupBottomNav()
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
                        R.id.account -> refresh(R.id.accountContainerFragment)
                    }
                }
            }
        }
    }

    private fun refresh(@IdRes destinationId: Int) {
        findNavController(R.id.main_container)
            .popBackStack(destinationId, false)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(destination.id != R.id.districtsFragment)
    }

}