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
    private val statisticBtnClickListener: OnStatisticBtnClicklistener,
    private val retryPageClickListener: () -> Unit
) : ListAdapter<FeedModel, PostViewHolder>(PostDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            PostViewHolder.Post.VIEW_TYPE -> PostViewHolder.Post(
                likeClickListener = { position ->
                    getPostOrNull(position)?.let {
                        likeBtnClickListener.onLikeBtnClicked(it, position)
                    }
                },
                dislikeClickListener = { position ->
                    getPostOrNull(position)?.let {
                        dislikeBtnClickListener.onDislikeBtnClicked(it, position)
                    }
                },
                avatarClickListener = { position ->
                    getPostOrNull(position)?.let {
                        avatarBtnClickListener.onAvatarBtnClicked(it, position)
                    }
                },
                statisticClickListener = { position ->
                    getPostOrNull(position)?.let {
                        statisticBtnClickListener.onStatisticBtnCliked(it, position)
                    }
                },
                view = inflater.inflate(R.layout.item_post, parent, false)
            )
            PostViewHolder.ErrorViewHolder.VIEW_TYPE -> {
                PostViewHolder.ErrorViewHolder(
                    retryListener = retryPageClickListener,
                    view = inflater.inflate(R.layout.item_error, parent, false)
                )
            }
            PostViewHolder.ProgressViewHolder.VIEW_TYPE -> {
                PostViewHolder.ProgressViewHolder(
                    inflater.inflate(
                        R.layout.item_progress,
                        parent,
                        false
                    )
                )
            }
            else -> error("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is FeedModel.Post -> PostViewHolder.Post.VIEW_TYPE
            FeedModel.Error -> PostViewHolder.ErrorViewHolder.VIEW_TYPE
            FeedModel.Progress -> PostViewHolder.ProgressViewHolder.VIEW_TYPE
        }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (holder is PostViewHolder.Post) {
            getPostOrNull(position)?.let(holder::bind)
        }
    }

    private fun getPostOrNull(position: Int): PostModel? =
        (getItem(position) as? FeedModel.Post)?.post

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
