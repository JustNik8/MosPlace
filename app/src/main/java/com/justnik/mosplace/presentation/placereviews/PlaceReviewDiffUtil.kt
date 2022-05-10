package com.justnik.mosplace.presentation.placereviews

import androidx.recyclerview.widget.DiffUtil
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.presentation.place.PlaceViewModel

object PlaceReviewDiffUtil : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}