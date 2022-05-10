package com.justnik.mosplace.presentation.disctrictplaces

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemPlaceBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.helpers.parsePlaceType
import kotlin.random.Random


class PlaceAdapter(private val context: Context) :
    ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(PlaceDiffUtil) {

    inner class PlaceViewHolder(val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

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

            val randomRating = Random.nextDouble(2.0, 5.0)
            tvItemPlaceRating.text = String.format("%.2f", randomRating)

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