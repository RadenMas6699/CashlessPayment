/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.radenmas.cashless_payment.databinding.ActivityAuthBinding
import com.radenmas.cashless_payment.ui.user.UserMainActivity

class AuthActivity : AppCompatActivity() {

    lateinit var b: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        b = ActivityAuthBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        initView()
        onClick()
    }

    private fun onClick() {

    }

    private fun initView() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val myAccount = getSharedPreferences("myAccount", MODE_PRIVATE)
            val role = myAccount.getString("role", "")

            if (role == "user") {
                startActivity(Intent(this, UserMainActivity::class.java))
                finish()
            }
//            else if (role == "admin") {
//                startActivity(Intent(this, AdminActivity::class.java))
//                finish()
//            }
        }
    }
}