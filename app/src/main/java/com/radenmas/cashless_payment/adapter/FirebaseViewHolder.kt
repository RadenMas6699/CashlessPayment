/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radenmas.cashless_payment.R

class FirebaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // List Transaksi
    var imgType: ImageView? = null
    var tvType: TextView? = null
    var tvBalance: TextView? = null
    var tvDesc: TextView? = null
    var tvStatus: TextView? = null
    var tvTimestamp: TextView? = null

    init {
        // List Transaksi
        imgType = itemView.findViewById(R.id.imgType)
        tvType = itemView.findViewById(R.id.tvType)
        tvBalance = itemView.findViewById(R.id.tvBalance)
        tvDesc = itemView.findViewById(R.id.tvDesc)
        tvStatus = itemView.findViewById(R.id.tvStatus)
        tvTimestamp = itemView.findViewById(R.id.tvTimestamp)

    }
}