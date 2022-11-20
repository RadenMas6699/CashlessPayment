/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.radenmas.cashless_payment.databinding.ActivityAuthBinding
import com.radenmas.cashless_payment.ui.user.UserMainActivity
import com.radenmas.cashless_payment.utils.Utils

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
            startActivity(Intent(this, UserMainActivity::class.java))
            finish()
        }

        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun requestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission
            ) -> {
                Utils.toast(this, "Permission Required")
                requestPermissionLauncher.launch(
                    permission
                )
            }
            else -> {
                requestPermissionLauncher.launch(
                    permission
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
}