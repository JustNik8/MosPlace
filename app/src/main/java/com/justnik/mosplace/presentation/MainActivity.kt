package com.justnik.mosplace.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.R
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
        loadDistricts()
    }

    private fun loadDistricts(){
        scope.launch {
            val districts = viewModel.loadDistricts()
            Log.d("GetRequest", districts.toString())
        }
    }
}