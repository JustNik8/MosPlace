package com.justnik.mosplace.presentation.adapters.placeimages

import android.content.Context
import android.view.ContentInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.justnik.mosplace.databinding.ItemPlaceImageBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class PlaceImageSliderAdapter(
    private val imageUrls: List<String>,
    private val context: Context
    ) : SliderViewAdapter<PlaceImageSliderAdapter.PlaceImageViewHolder>() {


    inner class PlaceImageViewHolder(val binding: ItemPlaceImageBinding) :
        ViewHolder(binding.root)

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): PlaceImageViewHolder {
        val binding = ItemPlaceImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaceImageViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: PlaceImageViewHolder, position: Int) {
        val binding = viewHolder.binding

        Glide.with(context).load(imageUrls[position]).into(binding.ivSliderPlaceImage)
    }
}