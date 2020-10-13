package com.example.tribune.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import com.example.tribune.getTimeAgo
import kotlinx.android.synthetic.main.item_post.view.*
import splitties.toast.toast

class PostAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var dislikeBtnClickListener: OnDislikeBtnClickListener? = null
    var avatarBtnClickListener: OnAvatarBtnClickListener? = null
    var statisticBtnClickListener: OnStatisticBtnClicklistener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val postView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(this, postView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position])
        }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnDislikeBtnClickListener {
        fun onDislikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnAvatarBtnClickListener {
        fun onAvatarBtnClicked(item: PostModel, position: Int)
    }

    interface OnStatisticBtnClicklistener {
        fun onStatisticBtnCliked(item: PostModel, position: Int)
    }
}

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.likeActionPerforming) {
                        context.toast(R.string.like_in_progress)
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }

            dislikeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.dislikeActionPerforming) {
                        context.toast(R.string.dislike_is_progress)
                    } else {
                        adapter.dislikeBtnClickListener?.onDislikeBtnClicked(item, currentPosition)
                    }
                }
            }

            avatarBtn_item.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    adapter.avatarBtnClickListener?.onAvatarBtnClicked(item, currentPosition)
                }
            }

            statisticBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    adapter.statisticBtnClickListener?.onStatisticBtnCliked(item, currentPosition)
                }
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
            avatarBtn_item.setImageResource(R.drawable.ic_avatar_48dp)

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
            } else if (post.likedByMe) {
                likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_for_me_alt_24)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            } else {
                likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }

            if (post.dislikeActionPerforming) {
                dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
            } else if (post.dislikedByMe) {
                dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_for_me_alt_24)
                dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                dislikeBtn.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
                dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }
        }
    }
}

