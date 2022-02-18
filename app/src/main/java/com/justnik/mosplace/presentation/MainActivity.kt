package com.justnik.mosplace.presentation

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.R
import com.justnik.mosplace.domain.entities.District
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val viewModel: MosViewModel by lazy {
        ViewModelProvider(this)[MosViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchDistrictsFragment()
        requestPlaces()

    }

    private fun requestPlaces(){
        scope.launch {
            val places = viewModel.loadPlacesByDistrictId(1)
            Log.d("Places", places.toString())
        }
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

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}