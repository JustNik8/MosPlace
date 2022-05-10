package com.justnik.mosplace.presentation.placereviews

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.R
import com.justnik.mosplace.databinding.ItemPlaceReviewBinding
import com.justnik.mosplace.domain.entities.Review

class PlaceReviewsAdapter(private val context: Context) :
    ListAdapter<Review, PlaceReviewsAdapter.PlaceReviewViewHolder>(PlaceReviewDiffUtil) {

    inner class PlaceReviewViewHolder(val binding: ItemPlaceReviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceReviewViewHolder {
        val binding = ItemPlaceReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceReviewViewHolder, position: Int) {
        val binding = holder.binding
        val review = getItem(position)

        with(binding) {
            tvItemReviewName.text = review.profileName
            rbItemReview.rating = review.rating.toFloat()
            tvItemReviewDate.text = review.date
            tvItemReviewText.text = review.text

            Glide.with(context).load(review.profileImageUrl).into(ivItemReviewAvatar)

            tvViewOptions.setOnClickListener {
                showPopupMenu(binding)
            }
        }
    }

    private fun showPopupMenu(binding: ItemPlaceReviewBinding) {
        val popup = PopupMenu(context, binding.tvViewOptions)
        popup.inflate(R.menu.options_menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_mark_as_spam -> {
                    Snackbar.make(
                        binding.root,
                        context.getString(R.string.review_spam_marked),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            true
        }
        popup.show()
    }
}