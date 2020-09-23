package com.example.tribune.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import com.example.tribune.Repository
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.item_load_more.view.*
import kotlinx.android.synthetic.main.item_load_more.view.progressbar
import kotlinx.android.synthetic.main.item_load_new.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import splitties.toast.toast

class PostAdapter(val list: MutableList<PostModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_TYPE_POST = 1
    private val ITEM_TYPE_REPOST = 2
    private val ITEM_FOOTER = 3;
    private val ITEM_HEADER = 4

    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostBtnClickListener: OnRepostBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_POST) {
            val postView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            PostViewHolder(this, postView)
        } else if (viewType == ITEM_TYPE_REPOST) {
            val repostView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_repost, parent, false)
            RepostViewHolder(this, repostView)
        } else if (viewType == ITEM_HEADER) {
            HeaderViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(R.layout.item_load_new, parent, false)
            )
        } else {
            FooterViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false)
            )
        }
    }


    override fun getItemCount() = list.size + 2

    override fun getItemViewType(position: Int): Int {
        Log.v("test", "getitem type by position " + position)
        return when {
            position == 0 -> ITEM_HEADER
            position == list.size + 1 -> ITEM_FOOTER
            // 0-я позиция header, 1-я позиция в адаптере
            // это 0-я позиция в списке постов. Соответственно,
            // инкрементируем все значения
            list[position - 1].source == null -> ITEM_TYPE_POST
            else -> ITEM_TYPE_REPOST
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(list[position - 1])
            is RepostViewHolder -> holder.bind(list[position - 1])
        }
    }


    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostBtnClickListener {
        fun onRepostBtnClicked(item: PostModel, position: Int)
    }
}


class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition - 1]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            repostBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition - 1]
                    if (item.repostedByMe) {
                        context.toast("Can't repost repost)")
                    } else {
                        showDialog(context) {
                            adapter.repostBtnClickListener?.onRepostBtnClicked(
                                item,
                                currentPosition
                            )
                        }
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.ownerName
            contentTv.text = post.content
            likesTv.text = post.likes.toString()
            repostsTv.text = post.reposts.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likedByMe) {
                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }

            if (post.repostActionPerforming) {
                repostBtn.setImageResource(R.drawable.ic_reposts_pending)
            } else if (post.repostedByMe) {
                repostBtn.setImageResource(R.drawable.ic_reposts_active)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                repostBtn.setImageResource(R.drawable.ic_reposts_inactive)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }
        }
    }
}

class RepostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            repostBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.repostedByMe) {
                        context.toast("Can't repost repost)")
                    } else {
                        showDialog(context) {
                            adapter.repostBtnClickListener?.onRepostBtnClicked(
                                item,
                                currentPosition
                            )
                        }
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.ownerName
            contentTv.text = post.content
            likesTv.text = post.likes.toString()
            repostsTv.text = post.reposts.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likedByMe) {
                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }

            if (post.repostActionPerforming) {
                repostBtn.setImageResource(R.drawable.ic_reposts_pending)
            } else if (post.repostedByMe) {
                repostBtn.setImageResource(R.drawable.ic_reposts_active)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                repostBtn.setImageResource(R.drawable.ic_reposts_inactive)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }
        }
    }
}

class HeaderViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            // Слушатель на кнопку
            loadNewBtn.setOnClickListener {
                // делаем кнопку неактивной пока идет запрос
                loadNewBtn.isEnabled = false
                // над кнопкой покажем progressBar
                progressbar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    // запрашиваем все посты после нашего первого поста
                    // (он же самый последний)
                    val response = Repository.getPostsAfter(adapter.list[0].id)
                    // восстанавливаем справедливость
                    progressbar.visibility = View.INVISIBLE
                    loadNewBtn.isEnabled = true
                    if (response.isSuccessful) {
                        // Если все успешно, то новые элементы добавляем в начало
                        // нашего списка.
                        val newItems = response.body()!!
                        adapter.list.addAll(0, newItems)
                        // Оповещаем адаптер о новых элементах
                        adapter.notifyItemRangeInserted(0, newItems.size)
                    } else {
                        context.toast("Error occured")
                    }
                }
            }
        }
    }
}

class FooterViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            // Слушатель на кнопку
            loadMoreBtn.setOnClickListener {
                // делаем кнопку неактивной пока идет запрос
                loadMoreBtn.isEnabled = false
                // над кнопкой покажем progressBar
                progressbar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    // запрашиваем все посты после нашего первого поста
                    // (он же самый последний)
                    val response = Repository.getPostsBefore(adapter.list[0].id)
                    // восстанавливаем справедливость
                    progressbar.visibility = View.INVISIBLE
                    loadMoreBtn.isEnabled = true
                    if (response.isSuccessful) {
                        // Если все успешно, то новые элементы добавляем в начало
                        // нашего списка.
                        val newItems = response.body()!!
                        adapter.list.addAll(0, newItems)
                        // Оповещаем адаптер о новых элементах
                        adapter.notifyItemRangeInserted(0, newItems.size)
                    } else {
                        context.toast("Error occured")
                    }
                }
            }
        }
    }
}

fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
    val dialog = AlertDialog.Builder(context)
        .setView(R.layout.activity_create_post)
        .show()
    dialog.createPostBtn.setOnClickListener {
        createBtnClicked(dialog.contentEdt.text.toString())
        dialog.dismiss()
    }
}

