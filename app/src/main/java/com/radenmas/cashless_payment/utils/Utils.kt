/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.radenmas.cashless_payment.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Created by RadenMas on 21/03/2022.
 */
object Utils {
    private lateinit var loading: Dialog
    private lateinit var resultSuccess: Dialog

    fun showLoading(context: Context) {
        loading = Dialog(context)
        loading.setContentView(R.layout.dialog_progress)
        loading.window!!.setBackgroundDrawableResource(R.drawable.bg_progress)
        loading.show()
    }

    fun dismissLoading() {
        loading.dismiss()
    }

    fun showResultSuccess(context: Context, title: String, desc: String, navigation: Unit) {
        resultSuccess = Dialog(context)
        resultSuccess.setContentView(R.layout.dialog_result_success)
        resultSuccess.window!!.setBackgroundDrawableResource(R.drawable.bg_progress)
        resultSuccess.show()

        val titleResultSuccess = resultSuccess.findViewById<TextView>(R.id.titleResult)
        val descResultSuccess = resultSuccess.findViewById<TextView>(R.id.descResult)
        val btnResultSuccess = resultSuccess.findViewById<MaterialButton>(R.id.btnResult)

        titleResultSuccess.text = title
        descResultSuccess.text = desc
        btnResultSuccess.setOnClickListener {
            resultSuccess.dismiss()
        }
    }

    fun dismissResultSuccess() {
        resultSuccess.dismiss()
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun snack(view: View, msg: String) {
        Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun snackInfinite(view: View, msg: String): Snackbar {
        return Snackbar.make(
            view,
            msg, Snackbar.LENGTH_INDEFINITE
        )
    }

    fun formatDateFull(timestamp: Long): String {
        val formatDate = SimpleDateFormat(
            "EEEE, dd LLLL yyyy 'pukul' HH:mm", Locale("ID")
        )
        val date = Date(timestamp)
        return formatDate.format(date)
    }

    fun formatDateSimple(timestamp: Long): String {
        val formatDate = SimpleDateFormat(
            "dd-MM-yyyy", Locale("ID")
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

    fun formatUsage(value: String): String {
//        return "$value M\u00B3"
        return "$value L"
    }

    fun formatToken(value: String): String {
        var number = value
        number = number.substring(0, number.length - 4) + "-" + number.substring(
            number.length - 4,
            number.length
        )
        number = number.substring(0, number.length - 8) + ")" + number.substring(
            number.length - 8,
            number.length
        )
        number = number.substring(0, number.length - 12) + "(" + number.substring(
            number.length - 12,
            number.length
        )
        return number
    }

    fun formatPeriod(month: String, year: String): String {
        var bulan: String? = null
        when (month) {
            "1" -> {
                bulan = "Januari"
            }
            "2" -> {
                bulan = "Februari"
            }
            "3" -> {
                bulan = "Maret"
            }
            "4" -> {
                bulan = "April"
            }
            "5" -> {
                bulan = "Mei"
            }
            "6" -> {
                bulan = "Juni"
            }
            "7" -> {
                bulan = "Juli"
            }
            "8" -> {
                bulan = "Agustus"
            }
            "9" -> {
                bulan = "September"
            }
            "10" -> {
                bulan = "Oktober"
            }
            "11" -> {
                bulan = "November"
            }
            "12" -> {
                bulan = "Desember"
            }
        }
        return "$bulan $year"
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