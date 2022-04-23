package com.justnik.mosplace.presentation.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityOnBoardingBinding
import com.justnik.mosplace.domain.entities.OnBoardingItem
import com.justnik.mosplace.presentation.MainActivity

class OnBoardingActivity : AppCompatActivity(R.layout.activity_on_boarding) {

    private val binding: ActivityOnBoardingBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPager()
        setClickListeners()
    }


    private fun setupPager() {
        val pagerAdapter = OnBoardingAdapter(this, createItems())
        binding.onBoardingPager.adapter = pagerAdapter

        TabLayoutMediator(binding.onBoardingTabLayout, binding.onBoardingPager)
        { _, _ ->}.attach()
    }

    private fun setClickListeners() {
        binding.bNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("open", false)
            startActivity(intent)
        }
    }

    private fun createItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem("Title1", "Description1"),
            OnBoardingItem("Title2", "Description2"),
            OnBoardingItem("Title3", "Description3")
        )
    }

}