package com.justnik.mosplace.presentation.adapters.district

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemDistrictBinding
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.helpers.getAbbreviationWithName


class DistrictAdapter(private val context: Context) :
    ListAdapter<District, DistrictAdapter.DistrictViewHolder>(DistrictDiffUtil) {

    class DistrictViewHolder(val binding: ItemDistrictBinding) :
        RecyclerView.ViewHolder(binding.root)

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

            Glide.with(context)
                .load(district.imageUrl)
                .error(R.drawable.no_image)
                .into(ivItemDistrictPhoto)
            if (district.imageUrl != null) {
                Glide.with(context).load(district.imageUrl).into(ivItemDistrictPhoto)
            } else {
                Glide.with(context).load(R.drawable.img_district_example).into(ivItemDistrictPhoto)
            }

            val districtText = getAbbreviationWithName(district.abbreviation, district.title)
            tvItemDistrictName.text = districtText

            root.setOnClickListener {
                onDistrictClickListener?.invoke(district)
            }

        }
    }
}