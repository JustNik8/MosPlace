package com.justnik.mosplace.presentation.start.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.FragmentOnBoardingBinding
import com.justnik.mosplace.domain.entities.OnBoardingItem
import com.justnik.mosplace.helpers.prefs.UserPrefs

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding: FragmentOnBoardingBinding by viewBinding()

    private val pagerAdapter by lazy { OnBoardingAdapter(requireContext(), createItems()) }

    private val userPrefs: UserPrefs by lazy { UserPrefs(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPager()
        setClickListeners()
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
            }
        }

        binding.bSkip.setOnClickListener {
            finishOnBoarding()
        }

        binding.bGetStarted.setOnClickListener {
            finishOnBoarding()
        }

        binding.onBoardingTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setupGetStartedButton(tab.position, pagerAdapter.itemCount)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { /*Do nothing*/
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { /*Do nothing*/
            }
        })
    }

    private fun finishOnBoarding() {
        userPrefs.isOnBoardingFinished = true
        val direction = OnBoardingFragmentDirections.actionOnBoardingFragmentToAuthFragment()
        findNavController().navigate(direction)
    }

    private fun setupGetStartedButton(currentPosition: Int, itemCount: Int) {
        //Check if current position is last index
        if (currentPosition == itemCount - 1) {
            showGetStartedButton()
            //Check if current position is penultimate index
        } else if (currentPosition == itemCount - 2) {
            hideGetStartedButton()
        }
    }

    private fun showGetStartedButton() {
        binding.bGetStarted.visibility = View.VISIBLE
        binding.bNext.visibility = View.GONE
        binding.bSkip.visibility = View.GONE
    }

    private fun hideGetStartedButton() {
        binding.bGetStarted.visibility = View.GONE
        binding.bNext.visibility = View.VISIBLE
        binding.bSkip.visibility = View.VISIBLE
    }


    private fun createItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(getString(R.string.screen1_title), getString(R.string.screen1_desc), R.drawable.human_running),
            OnBoardingItem(getString(R.string.screen2_title), getString(R.string.screen2_desc), R.drawable.human_sitting),
            OnBoardingItem(getString(R.string.screen3_title), getString(R.string.screen3_desc), R.drawable.human_standing)
        )
    }
}