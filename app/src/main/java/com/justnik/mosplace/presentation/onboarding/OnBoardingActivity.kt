package com.justnik.mosplace.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ActivityOnBoardingBinding
import com.justnik.mosplace.domain.entities.OnBoardingItem
import com.justnik.mosplace.helpers.UserPrefs
import com.justnik.mosplace.presentation.MainActivity

class OnBoardingActivity : AppCompatActivity(R.layout.activity_on_boarding) {

    private val binding: ActivityOnBoardingBinding by viewBinding()

    private val pagerAdapter by lazy {
        OnBoardingAdapter(this, createItems())
    }

    private val userPrefs: UserPrefs by lazy {
        UserPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkOnBoardingIfFinished()
        setupPager()
        setClickListeners()
    }

    private fun checkOnBoardingIfFinished(){
        if (userPrefs.isOnBoardingFinished){
            openMainActivity()
        }
    }

    private fun setupPager() {
        binding.onBoardingPager.adapter = pagerAdapter

        TabLayoutMediator(binding.onBoardingTabLayout, binding.onBoardingPager)
        { _, _ -> }.attach()
    }

    private fun setClickListeners() {
        binding.bNext.setOnClickListener {
            val currentPosition = binding.onBoardingTabLayout.selectedTabPosition
            val itemCount = pagerAdapter.itemCount

            if (currentPosition + 1 < itemCount) {
                binding.onBoardingPager.currentItem = binding.onBoardingPager.currentItem + 1
            } else {
                userPrefs.isOnBoardingFinished = true
                openMainActivity()
            }
        }

        binding.bSkip.setOnClickListener {
            userPrefs.isOnBoardingFinished = true
            openMainActivity()
        }

        binding.onBoardingTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setNextButtonText(tab.position, pagerAdapter.itemCount)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {/*Do nothing*/}
            override fun onTabReselected(tab: TabLayout.Tab?) { /*Do nothing*/ }
        })
    }

    private fun setNextButtonText(currentPosition: Int, itemCount: Int) {
        Log.d("RRR", "$currentPosition, $itemCount")

        //Check if current position is last index
        if (currentPosition == itemCount - 1) {
            binding.bNext.text = getString(R.string.finish)
        //Check if current position is penultimate index
        } else if (currentPosition == itemCount - 2) {
            binding.bNext.text = getString(R.string.next)
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(getString(R.string.screen1_title), getString(R.string.screen1_desc)),
            OnBoardingItem(getString(R.string.screen2_title), getString(R.string.screen2_desc)),
            OnBoardingItem(getString(R.string.screen3_title), getString(R.string.screen3_desc))
        )
    }

}