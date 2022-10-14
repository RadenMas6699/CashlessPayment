/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.radenmas.cashless_payment.databinding.ActivityUserMainBinding

class UserMainActivity : AppCompatActivity() {

    lateinit var b: ActivityUserMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        b = ActivityUserMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        initView()
        onClick()
    }

    private fun onClick() {

    }

    private fun initView() {
//        val navController = findNavController(R.id.fragmentUserMain)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.userHomeFragment -> showBottomNav(true)
//                R.id.userCreateFragment -> showBottomNav(true)
//                R.id.userProfileFragment -> showBottomNav(true)
//                else -> showBottomNav(false)
//            }
//        }
//        b.navBottomUser.setupWithNavController(navController)
//
//        val uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
//        FirebaseMessaging.getInstance().token.addOnSuccessListener {
//            val token = it.toString()
//            FirebaseDatabase.getInstance().reference.child("User").child(uid).child("token")
//                .setValue(token)
//        }
    }

    private fun showBottomNav(params: Boolean) {
//        b.navBottomUser.visibility = if (params) {
//            View.VISIBLE
//        } else {
//            View.GONE
//        }
    }
}