package com.example.tribune.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tribune.R
import kotlinx.android.synthetic.main.activity_author_page.*
import splitties.toast.toast

class AuthorPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_page)

        avatarBtn.setOnClickListener {
            toast("Click!")
        }
    }

}