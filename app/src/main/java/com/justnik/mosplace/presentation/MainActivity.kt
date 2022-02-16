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

        launchDistrictsFragment()
    }

    private fun launchDistrictsFragment(){
        val fragment = DistrictsFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()

    }
}