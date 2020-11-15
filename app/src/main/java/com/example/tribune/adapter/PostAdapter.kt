package com.example.tribune.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R

class PostAdapter(
    private val likeBtnClickListener: OnLikeBtnClickListener,
    private val dislikeBtnClickListener: OnDislikeBtnClickListener,
    private val avatarBtnClickListener: OnAvatarBtnClickListener,
    private val statisticBtnClickListener: OnStatisticBtnClicklistener
) : ListAdapter<PostModel, PostViewHolder>(PostDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(
            likeClickListener = {
                likeBtnClickListener.onLikeBtnClicked(getItem(it), it)
            },
            dislikeClickListener = {
                dislikeBtnClickListener.onDislikeBtnClicked(getItem(it), it)
            },
            avatarClickListener = {
                avatarBtnClickListener.onAvatarBtnClicked(getItem(it), it)
            },
            statisticClickListener = {
                statisticBtnClickListener.onStatisticBtnCliked(getItem(it), it)
            },
            view = postView
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    fun interface OnDislikeBtnClickListener {
        fun onDislikeBtnClicked(item: PostModel, position: Int)
    }

    fun interface OnAvatarBtnClickListener {
        fun onAvatarBtnClicked(item: PostModel, position: Int)
    }

    fun interface OnStatisticBtnClicklistener {
        fun onStatisticBtnCliked(item: PostModel, position: Int)
    }
}
