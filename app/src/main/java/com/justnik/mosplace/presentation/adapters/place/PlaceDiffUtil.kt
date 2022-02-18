package com.justnik.mosplace.presentation.adapters.place

import androidx.recyclerview.widget.DiffUtil
import com.justnik.mosplace.domain.entities.Place

object PlaceDiffUtil : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}