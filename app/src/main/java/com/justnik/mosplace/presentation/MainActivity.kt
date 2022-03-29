package com.justnik.mosplace.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_container
        ) as NavHostFragment

        val navController = navHostFragment.navController
        val bottomNavigationView = binding.mainBottomNavigation

        bottomNavigationView.setupWithNavController(navController)
    }

}