package com.example.tribune.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tribune.R
import kotlinx.android.synthetic.main.activity_registeration.*

class Registeration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
    }

    val registerBtn = registration_btn_register
    val haveAccountBtn = registration_btn_have_account
    val userNameEdTxt = registration_edtxt_username
    val userPassword = registration_edtxt_password
    val confirnPassword = registration_edtxt_confirm
}