/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.radenmas.cashless_payment.databinding.ActivityAuthBinding
import com.radenmas.cashless_payment.ui.user.UserMainActivity

class AuthActivity : AppCompatActivity() {

    lateinit var b: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAuthBinding.inflate(layoutInflater)
        b.root.apply { setContentView(this) }

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            startActivity(Intent(this, UserMainActivity::class.java))
            finish()
        }
    }
}