package com.justnik.mosplace.presentation.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemOnboardingBinding
import com.justnik.mosplace.domain.entities.OnBoardingItem

class OnBoardingAdapter(private val context: Context, private val items: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    inner class OnBoardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val binding = holder.binding
        val item = items[position]
        with(binding) {
            tvItemTitle.text = item.title
            tvItemDesc.text = item.desc
            Glide.with(context).load(R.drawable.no_image).into(ivItemOnBoarding)
        }
    }


    override fun getItemCount() = items.size

}