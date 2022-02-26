package com.justnik.mosplace.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityMainBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeBottomNavigation()
        binding.mainBottomNavigation.selectedItemId = R.id.page_main
    }

    private fun observeBottomNavigation() {
        binding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.page_main -> launchDistrictsFragment()
            }
            true
        }
    }

    private fun launchDistrictsFragment() {
        val fragment = DistrictsFragment.newInstance()
        fragment.onDistrictCLickListener = {
            launchDistrictPlacesFragment(it)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun launchDistrictPlacesFragment(district: District) {
        val fragment = DistrictPlacesFragment.newInstance(district)

        fragment.onPlaceClickListener = {
            launchPlaceFragment(it)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun launchPlaceFragment(place: Place) {
        val fragment = PlaceFragment.newInstance(place)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}