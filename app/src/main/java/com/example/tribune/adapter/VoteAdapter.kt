package com.example.tribune.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import kotlinx.android.synthetic.main.item_statistic.view.*

class VoteAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val postView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_statistic, parent, false)
        return VoteViewHolder(this, postView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VoteViewHolder -> holder.bind(list[position])
        }
    }
}

class VoteViewHolder(val adapter: VoteAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            vote_username.text =  post.ownerName.toString()
        }
    }
}
