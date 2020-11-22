package com.example.tribune.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.android_krud_app.dto.PostModel

object PostDiffer: DiffUtil.ItemCallback<FeedModel>() {
    override fun areItemsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean =
        oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean =
        oldItem == newItem
}