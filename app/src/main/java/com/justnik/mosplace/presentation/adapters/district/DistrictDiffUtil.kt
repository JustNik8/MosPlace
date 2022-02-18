package com.justnik.mosplace.presentation.adapters.district

import androidx.recyclerview.widget.DiffUtil
import com.justnik.mosplace.domain.entities.District

object DistrictDiffUtil : DiffUtil.ItemCallback<District>() {
    override fun areItemsTheSame(oldItem: District, newItem: District): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: District, newItem: District): Boolean {
        return oldItem == newItem
    }
}