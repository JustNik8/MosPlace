package com.justnik.mosplace.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.R
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val viewModel: MosViewModel by lazy {
        ViewModelProvider(this)[MosViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchDistrictsFragment()
    }

    private fun launchDistrictsFragment(){
        val fragment = DistrictsFragment.newInstance()
        fragment.onDistrictCLickListener = {
            launchDistrictPlacesFragment(it)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun launchDistrictPlacesFragment(district: District){
        val fragment = DistrictPlacesFragment.newInstance(district)

        fragment.onPlaceClickListener = {
            launchPlaceFragment(it)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun launchPlaceFragment(place: Place){
        val fragment = PlaceFragment.newInstance(place)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}