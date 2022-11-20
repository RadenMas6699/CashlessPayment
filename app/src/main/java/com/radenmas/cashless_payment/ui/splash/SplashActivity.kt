/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.ActivitySplashBinding
import com.radenmas.cashless_payment.ui.auth.AuthActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var b: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        initView()
        onClick()
    }

    private fun onClick() {

    }

    private fun initView() {
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        b.tvAppVersion.text = resources.getString(R.string.app_version, versionName)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, 1500)
    }
}