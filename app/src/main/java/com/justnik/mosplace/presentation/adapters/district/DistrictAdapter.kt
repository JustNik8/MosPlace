package com.justnik.mosplace.presentation.adapters.district

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemDistrictBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.getAbbreviationWithName


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

        with(binding) {

            if (district.imageUrl != null) {
                Glide.with(context).load(district.imageUrl).into(ivItemDistrictPhoto)
            } else {
                Glide.with(context).load(R.drawable.img_district_example).into(ivItemDistrictPhoto)
            }

            val districtText = getAbbreviationWithName(district.abbreviation, district.title)
            binding.tvItemDistrictName.text = districtText

            root.setOnClickListener {
                onDistrictClickListener?.invoke(district)
            }

        }

    }
}