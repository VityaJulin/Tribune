package com.example.tribune

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.activity.AuthorPage
import com.example.tribune.adapter.PostAdapter
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnAvatarBtnClickListener, PostAdapter.OnStatisticBtnClicklistener {
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener {
            start<CreatePostActivity>()
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(this@FeedActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = Repository.getPosts()
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(
                        (result.body() ?: emptyList()) as MutableList<PostModel>
                    ).apply {
                        likeBtnClickListener = this@FeedActivity
                        dislikeBtnClickListener = this@FeedActivity
                        avatarBtnClickListener = this@FeedActivity
                        statisticBtnClickListener = this@FeedActivity
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            item.likeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                if (item.likedByMe || item.dislikedByMe) {
                    toast(R.string.error_double_vote)
                } else {
                    Repository.likedByMe(item.id)
                    val response = Repository.likedByMe(item.id)
                    if (response.isSuccessful) {
                        item.updateLikes(response.body()!!)
                    }
                    adapter?.notifyItemChanged(position)
                }
                item.likeActionPerforming = false
            }
        }
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            item.dislikeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                if (item.likedByMe || item.dislikedByMe) {
                    toast(R.string.error_double_vote)
                } else {
                    Repository.dislikedByMe(item.id)
                    val response = Repository.dislikedByMe(item.id)
                    if (response.isSuccessful) {
                        item.updateDislikes(response.body()!!)
                        adapter?.notifyItemChanged(position)
                    }
                }
                item.dislikeActionPerforming = false
            }
        }
    }

    override fun onAvatarBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            toast("Click!")
            start<AuthorPage>()
        }
    }

    override fun onStatisticBtnCliked(item: PostModel, position: Int) {
        toast("click2!")
    }

    private fun refreshData() {
        lifecycleScope.launch {
            val newData = Repository.getRecent()
            swipeContainer.isRefreshing = false
            if (newData.isSuccessful) {
                container.adapter = PostAdapter(newData.body()!! as MutableList<PostModel>)
                container.adapter?.notifyDataSetChanged()
            }
        }
    }
}
