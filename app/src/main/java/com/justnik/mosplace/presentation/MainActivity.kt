package com.justnik.mosplace.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justnik.mosplace.ApiKeys
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityMainBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(ApiKeys.mapKit);

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeBottomNavigation()
        binding.mainBottomNavigation.selectedItemId = R.id.page_main
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeBottomNavigation() {
        binding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.page_main -> launchDistrictsFragment()
                R.id.page_map -> launchMapFragment()
                R.id.page_account -> launchAccountFragment()
            }
            true
        }
    }

    private fun launchAccountFragment() {
        val fragment = AccountFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun launchMapFragment() {
        val fragment = MapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
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