package com.justnik.mosplace.presentation.adapters.place

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.justnik.mosplace.databinding.ItemPlaceBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType


class PlaceAdapter(private val context: Context):
    ListAdapter<Place, PlaceViewHolder>(PlaceDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val binding = holder.binding
        val place = getItem(position)

        binding.place = place
        binding.tvItemPlaceType.text = parsePlaceType(place.type, context)

        Glide.with(context).load(place.images[0].imageUrl).into(binding.ivItemPlaceImage)
    }
}