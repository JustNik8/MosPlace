package com.justnik.mosplace.presentation.place

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemPlaceImageBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class PlaceImageSliderAdapter(
    private val imageUrls: List<String>,
    private val context: Context
) : SliderViewAdapter<PlaceImageSliderAdapter.PlaceImageViewHolder>() {


    inner class PlaceImageViewHolder(val binding: ItemPlaceImageBinding) :
        ViewHolder(binding.root)

    override fun getCount(): Int {
        return if (imageUrls.isNotEmpty()) imageUrls.size else 1
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

        with (binding) {
            if (imageUrls.isNotEmpty()) {
                Glide.with(context).load(imageUrls[position]).into(ivSliderPlaceImage)
            } else{
                Glide.with(context).load(R.drawable.no_image).into(ivSliderPlaceImage)
            }
        }
    }
}