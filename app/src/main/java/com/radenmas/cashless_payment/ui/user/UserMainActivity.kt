/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.ActivityUserMainBinding
import com.radenmas.cashless_payment.ui.scan.ScanActivity
import com.radenmas.cashless_payment.utils.Utils

class UserMainActivity : AppCompatActivity() {

    lateinit var b: ActivityUserMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        b = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.messaging.subscribeToTopic(uid)

        initView()
        onClick()
    }

    private fun onClick() {
        b.fab.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
    }

    private fun initView() {
        val navController = findNavController(R.id.fragmentUserMain)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.userHomeFragment -> showBottomNav(true)
                R.id.userProfileFragment -> showBottomNav(true)
                else -> showBottomNav(false)
            }
        }
        b.navBottomUser.setupWithNavController(navController)

        Firebase.messaging.isAutoInitEnabled = true
    }

    private fun showBottomNav(params: Boolean) {
        b.bottomAppBar.visibility = if (params) {
            View.VISIBLE
        } else {
            View.GONE
        }
        b.fab.visibility = if (params) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}