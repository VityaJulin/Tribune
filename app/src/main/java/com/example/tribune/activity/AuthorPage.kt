package com.example.tribune.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import com.example.tribune.Repository
import com.example.tribune.adapter.PostAdapter
import com.example.tribune.userId
import kotlinx.android.synthetic.main.activity_author_page.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class AuthorPage : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnStatisticBtnClicklistener {

    private var dialog: ProgressDialog? = null
    private var userId = intent.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_page)

        avatarBtn_author.setOnClickListener {
            toast("Change image")
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@AuthorPage).apply {
                setMessage(this@AuthorPage.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = Repository.getPostsByUserId(userId)
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container_author) {
                    layoutManager = LinearLayoutManager(this@AuthorPage)
                    adapter = PostAdapter(
                        (result.body() ?: emptyList()) as MutableList<PostModel>
                    ).apply {
                        likeBtnClickListener = this@AuthorPage
                        dislikeBtnClickListener = this@AuthorPage
                        statisticBtnClickListener = this@AuthorPage
                        authorTv.text = "1"
                        badgeTv.text = "2"
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
            with(container_author) {
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
            with(container_author) {
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

    override fun onStatisticBtnCliked(item: PostModel, position: Int) {
        lifecycleScope.launch {
            start<Statistic>()
        }
    }

}