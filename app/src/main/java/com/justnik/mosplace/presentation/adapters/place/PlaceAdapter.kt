package com.justnik.mosplace.presentation.adapters.place

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemPlaceBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType


class PlaceAdapter(private val context: Context) :
    ListAdapter<Place, PlaceViewHolder>(PlaceDiffUtil) {

    var onPlaceClickListener: ((Place) -> Unit)? = null

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

        with(binding) {
            tvItemPlaceTitle.text = place.title
            tvItemPlaceType.text = parsePlaceType(place.type, context)
            tvItemPlaceDesc.text = place.shortDescription

            root.setOnClickListener {
                onPlaceClickListener?.invoke(place)
            }
        }
        if (place.images.isNotEmpty()) {
            Glide.with(context).load(place.images[0].imageUrl).into(binding.ivItemPlaceImage)
        }
        else {
            Glide.with(context).load(R.drawable.no_image).into(binding.ivItemPlaceImage)
        }
    }
}