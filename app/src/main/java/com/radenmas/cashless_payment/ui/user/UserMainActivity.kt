/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        b = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(b.root)

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
        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.messaging.subscribeToTopic(uid)

        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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