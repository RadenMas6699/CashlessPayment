/*
 * Created by RadenMas on 5/11/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.Result
import com.radenmas.cashless_payment.databinding.ActivityScanBinding
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.utils.Utils
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*

/**
 * Created by RadenMas on 05/11/2022.
 */
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var b: ActivityScanBinding
    private lateinit var scanner: ZXingScannerView

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    val database = FirebaseDatabase.getInstance().reference

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        b = ActivityScanBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 5)
        }

        scanner = ZXingScannerView(this)
        scanner.setAutoFocus(true)
        b.frameCamera.addView(scanner)

        b.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun handleResult(p0: Result?) {
        val qrCode = p0?.text.toString().replace(".", "")

        // Cek QR Code
        // Cek Saldo User
        // Jika ada maka cek saldo admin
        // Saldo User - 10000
        // Saldo Admin + 10000

        checkQRCode(qrCode)

    }

    private fun checkQRCode(qrCode: String) {
        FirebaseDatabase.getInstance().reference.child("User").child(qrCode)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataAdmin = snapshot.getValue(User::class.java)
                        val balanceAdmin: Int = dataAdmin!!.balance!!
                        val nameAdmin: String = dataAdmin.username
                        checkSaldoUser(qrCode, balanceAdmin, nameAdmin)
                    } else {
                        Utils.toast(this@ScanActivity, "Merchant tidak terdaftar")
                        Handler(Looper.getMainLooper()).postDelayed({
                            onResume()
                        }, 1500)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun checkSaldoUser(qrCode: String, balanceAdmin: Int, nameAdmin: String) {
        FirebaseDatabase.getInstance().reference.child("User").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataUser = snapshot.getValue(User::class.java)
                        val balanceUser: Int = dataUser!!.balance!!
                        if (balanceUser >= 10000) {

                            val saldoAdmin = balanceAdmin + 10000
                            val saldoUser = balanceUser - 10000

                            val resultAdmin: MutableMap<String, Any> = HashMap()
                            resultAdmin["balance"] = saldoAdmin
                            database.child("User").child(qrCode).updateChildren(resultAdmin)

                            val resultUser: MutableMap<String, Any> = HashMap()
                            resultUser["balance"] = saldoUser
                            database.child("User").child(uid).updateChildren(resultUser)

                            val idHistory = Date().time

                            val pushHistory: MutableMap<String, Any> = HashMap()
                            pushHistory["id"] = idHistory.toString()
                            pushHistory["sender_id"] = uid
                            pushHistory["receive_id"] = qrCode
                            pushHistory["sender_name"] = dataUser.username
                            pushHistory["receive_name"] = nameAdmin
                            pushHistory["status"] = "Berhasil"
                            pushHistory["type"] = "Pembayaran"
                            pushHistory["balance"] = 10000
                            pushHistory["timestamp"] = idHistory

                            val titleSender = "Pembayaran Berhasil"
                            val bodySender =
                                "Kamu telah melakukan pembayaran sebesar ${Utils.formatRupiah(10000)} kepada $nameAdmin pada ${
                                    Utils.formatDateSimple(idHistory)
                                }."

                            val titleReceiver = "Dana Masuk"
                            val bodyReceiver =
                                "Kamu menerima dana sebesar ${Utils.formatRupiah(10000)} dari ${dataUser.username}  pada ${
                                    Utils.formatDateSimple(idHistory)
                                }."

                            Utils.sendNotification(
                               this@ScanActivity,
                                titleSender,
                                bodySender,
                                uid,
                                idHistory
                            )

                            Utils.sendNotification(
                                this@ScanActivity,
                                titleReceiver,
                                bodyReceiver,
                                qrCode,
                                idHistory
                            )

                            database.child("History").child(qrCode).child(idHistory.toString())
                                .setValue(pushHistory)
                            database.child("History").child(uid).child(idHistory.toString())
                                .setValue(pushHistory).addOnSuccessListener {
                                    onBackPressed()
                                }
                        } else {
                            Utils.toast(this@ScanActivity, "Saldo Anda tidak cukup")
                            onBackPressed()
                        }
                    } else {
                        Utils.toast(this@ScanActivity, "ERROR")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onResume() {
        super.onResume()
        scanner.setResultHandler(this)
        scanner.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scanner.stopCamera()
    }
}