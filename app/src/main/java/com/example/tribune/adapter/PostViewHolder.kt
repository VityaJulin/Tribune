package com.example.tribune.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import com.example.tribune.getTimeAgo
import kotlinx.android.synthetic.main.item_error.view.*
import kotlinx.android.synthetic.main.item_post.view.*

sealed class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
    class Post(
        private val likeClickListener: (position: Int) -> Unit,
        private val dislikeClickListener: (position: Int) -> Unit,
        private val avatarClickListener: (position: Int) -> Unit,
        private val statisticClickListener: (position: Int) -> Unit,
        view: View
    ) : PostViewHolder(view) {

        companion object {
            const val VIEW_TYPE = R.layout.item_post
        }

        init {
            with(itemView) {
                likeBtn.setOnClickListener {
                    likeClickListener(adapterPosition)
                }

                dislikeBtn.setOnClickListener {
                    dislikeClickListener(adapterPosition)
                }

                avatarBtn_item.setOnClickListener {
                    avatarClickListener(adapterPosition)
                }

                statisticBtn.setOnClickListener {
                    statisticClickListener(adapterPosition)
                }
            }
        }

        fun bind(post: PostModel) {
            with(itemView) {
                authorTv.text = post.ownerName
                contentTv.text = post.content
                likesTv.text = post.likes.toString()
                dislikesTv.text = post.dislikes.toString()
                createdTv.text = getTimeAgo(post.created)

                when {
                    post.likeActionPerforming -> {
                        likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
                    }
                    post.likedByMe -> {
                        likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_for_me_alt_24)
                        likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    }
                    else -> {
                        likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
                        likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
                    }
                }

                when {
                    post.dislikeActionPerforming -> {
                        dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
                    }
                    post.dislikedByMe -> {
                        dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_for_me_alt_24)
                        dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    }
                    else -> {
                        dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
                        dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
                    }
                }
            }
        }
    }
    class ErrorViewHolder(private val retryListener: () -> Unit, view: View): PostViewHolder(view) {
        companion object {
            const val VIEW_TYPE = R.layout.item_error
        }
        init {
            view.retryButton.setOnClickListener {
                retryListener()
            }
        }
    }
    class ProgressViewHolder(view: View): PostViewHolder(view) {
        companion object {
            const val VIEW_TYPE = R.layout.item_progress
        }
    }
}
