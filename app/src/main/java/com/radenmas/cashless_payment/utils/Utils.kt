/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.model.Notification
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Created by RadenMas on 21/03/2022.
 */
object Utils {
    private lateinit var loading: Dialog
    private lateinit var qrcode: Dialog

    const val FCM_API = "https://fcm.googleapis.com/fcm/send"
    const val serverKey =
        "key=AAAAEcSZtig:APA91bGhjXfPwDeFJa5fVzt-1DDnlFEYOJg1EroUP6LvW_nD5rcZ8tx36jDYGhErwJlgvufUXaIQWC17zVsPSt-NKBMJsipvLLQ33Op3gf_phm-xsOKNexHkWawMkNvMkP90WqSWdtam"
    const val contentType = "application/json"

    fun showLoading(context: Context) {
        loading = Dialog(context)
        loading.setContentView(R.layout.dialog_progress)
        loading.window!!.setBackgroundDrawableResource(R.drawable.bg_progress)
        loading.show()
    }

    fun dismissLoading() {
        loading.dismiss()
    }

    fun showQRCode(context: Context, uid: String) {
        qrcode = Dialog(context)
        qrcode.setContentView(R.layout.dialog_qr_code)
        qrcode.window!!.setBackgroundDrawableResource(R.drawable.bg_progress)
        qrcode.show()

        val titleResultSuccess = qrcode.findViewById<TextView>(R.id.tvUid)
        val imageQRCode = qrcode.findViewById<ImageView>(R.id.imgQRCode)

        titleResultSuccess.text = uid

        val matrix = MultiFormatWriter().encode(
            uid,
            BarcodeFormat.QR_CODE,
            300, 300
        )

        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        imageQRCode.setImageBitmap(bitmap)
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun sendNotification(
        context: Context,
        title: String,
        body: String,
        to: String,
        timestamp: Long
    ) {
        val notification = JSONObject()
        val notificationBody = JSONObject()

        notificationBody.put("title", title)
        notificationBody.put(
            "body",
            body
        )
        notification.put("to", "/topics/$to")
        notification.put("notification", notificationBody)

        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = object : JsonObjectRequest(
            FCM_API, notification,
            Response.Listener { _ ->
            },
            Response.ErrorListener {
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)

        val dataNotif = Notification(
            title,
            body,
            timestamp,
            false
        )
        FirebaseDatabase.getInstance().reference.child("Notification")
            .child(to).child(timestamp.toString()).setValue(dataNotif)
    }

    fun formatDateSimple(timestamp: Long): String {
        val formatDate = SimpleDateFormat(
            "dd MMM yyyy • HH:mm", Locale("ID")
        )
        val date = Date(timestamp)
        return formatDate.format(date)
    }

    fun formatNumber(value: Int): String {
        return String.format("%,d", value)
    }

    fun formatRupiah(value: Int): String {
        val number: String =
            String.format("%,d", value)
        return "Rp$number"
    }

    fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap? {
        val ratioSquare: Double
        val bitmapHeight: Int = bitmap.height
        val bitmapWidth: Int = bitmap.width
        ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = sqrt(ratioSquare)
        val requiredHeight = (bitmapHeight / ratio).roundToInt()
        val requiredWidth = (bitmapWidth / ratio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }
}