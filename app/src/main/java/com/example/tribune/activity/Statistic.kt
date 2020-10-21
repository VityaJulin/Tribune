package com.example.tribune.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_krud_app.dto.PostModel
import com.example.tribune.R
import com.example.tribune.Repository
import com.example.tribune.USER_ID
import com.example.tribune.adapter.VoteAdapter
import kotlinx.android.synthetic.main.activity_statistic.*
import kotlinx.coroutines.launch
import splitties.toast.toast

class Statistic : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var userId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        userId = intent.getLongExtra(USER_ID, 0L)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            dialog = ProgressDialog(this@Statistic).apply {
                setMessage(this@Statistic.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = Repository.getPostsByUserId(userId)
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container_statistic) {
                    layoutManager = LinearLayoutManager(this@Statistic)
                    adapter = VoteAdapter(
                        (result.body() ?: emptyList()) as MutableList<PostModel>
                    ).apply {
                        //statisticBtnClickListener = this@AuthorPage
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }
    }
}