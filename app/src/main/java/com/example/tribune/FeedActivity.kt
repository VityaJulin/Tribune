package com.example.tribune

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.adapter.PostAdapter
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class FeedActivity : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnRepostBtnClickListener {
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        fab.setOnClickListener {
            start<CreatePostActivity>()
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
                val response = if (item.likedByMe) {
                    Repository.cancelMyLike(item.id)
                } else {
                    Repository.likedByMe(item.id)
                }
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updateLikes(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            item.dislikeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)
                val response = Repository.repostedByMe(item.id)
                if (response.isSuccessful) {
                    item.updatePost(response.body()!!)
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }
}
