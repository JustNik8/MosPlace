package com.justnik.mosplace.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justnik.mosplace.ApiKeys
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityMainBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.presentation.account.AccountFragment
import com.justnik.mosplace.presentation.disctrictplaces.DistrictPlacesFragment
import com.justnik.mosplace.presentation.disctrictplaces.DistrictPlacesViewModel
import com.justnik.mosplace.presentation.districts.DistrictsFragment
import com.justnik.mosplace.presentation.map.MapFragment
import com.justnik.mosplace.presentation.place.PlaceFragment
import com.justnik.mosplace.presentation.review.ReviewFragment
import com.yandex.mapkit.MapKitFactory
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
        fragment.onReviewButtonClickListener = {
            launchReviewFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun launchReviewFragment(){
        val fragment = ReviewFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}