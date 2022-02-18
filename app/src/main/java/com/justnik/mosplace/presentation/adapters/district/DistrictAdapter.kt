package com.justnik.mosplace.presentation.adapters.district

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemDistrictBinding
import com.justnik.mosplace.domain.entities.District


class DistrictAdapter(private val context: Context) :
    ListAdapter<District, DistrictViewHolder>(DistrictDiffUtil) {

    var onDistrictClickListener: ((District) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        val binding = ItemDistrictBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DistrictViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        val binding = holder.binding
        val district = getItem(position)

        binding.district = district

        with(binding) {
            Glide.with(context).load(R.drawable.img_district_example).into(ivItemDistrictPhoto)

            root.setOnClickListener {
                onDistrictClickListener?.invoke(district)
            }
        }

    }
}