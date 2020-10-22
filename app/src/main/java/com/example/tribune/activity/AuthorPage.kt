package com.example.tribune.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.AttachmentModel
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.*
import com.example.tribune.adapter.PostAdapter
import kotlinx.android.synthetic.main.activity_author_page.*
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast
import java.io.IOException

class AuthorPage : AppCompatActivity(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnStatisticBtnClicklistener {

    private var dialog: ProgressDialog? = null
    private var userId = 0L
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_page)
        userId = intent.getLongExtra(USER_ID, 0L)
        avatarBtn_author.setOnClickListener {
            takePictureIntent()
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
            val authorInfo = Repository.getUserById(userId)
            dialog?.dismiss()
            if (result.isSuccessful || authorInfo.isSuccessful) {
                with(container_author) {
                    layoutManager = LinearLayoutManager(this@AuthorPage)
                    adapter = PostAdapter(
                        (result.body() ?: emptyList()) as MutableList<PostModel>
                    ).apply {
                        likeBtnClickListener = this@AuthorPage
                        dislikeBtnClickListener = this@AuthorPage
                        statisticBtnClickListener = this@AuthorPage
                        authorTv.text = authorInfo.body()!!.username
                        if (authorInfo.body()!!.isReadOnly) {
                            badgeTv.text = "Read only!"
                        } else {
                            badgeTv.text = "Author"
                        }
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
            val intent = Intent(this@AuthorPage, Statistic::class.java)
            intent.putExtra(USER_ID, item.ownerId)
            startActivity(intent)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.bitmap

            lifecycleScope.launch {
                try {
                    val imageUploadResult = Repository.upload(imageBitmap!!)
                    if (imageUploadResult.isSuccessful) {
                        attachmentModel = imageUploadResult.body()
                    } else {
                        toast("upload error")
                    }
                } catch (e: IOException) {
                    toast("image loading error")
                }

            }
        }
    }


    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}

