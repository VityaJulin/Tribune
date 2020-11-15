package com.example.tribune.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.android_krud_app.dto.PostModel

object PostDiffer: DiffUtil.ItemCallback<PostModel>() {
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
        oldItem == newItem
}