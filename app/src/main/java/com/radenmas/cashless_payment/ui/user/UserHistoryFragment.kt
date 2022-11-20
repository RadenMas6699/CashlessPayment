/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.adapter.FirebaseViewHolder
import com.radenmas.cashless_payment.databinding.FragmentUserHistoryBinding
import com.radenmas.cashless_payment.model.History
import com.radenmas.cashless_payment.utils.Utils

class UserHistoryFragment : Fragment() {

    private lateinit var b: FragmentUserHistoryBinding

    private lateinit var options: FirebaseRecyclerOptions<History>
    private lateinit var adapter: FirebaseRecyclerAdapter<History, FirebaseViewHolder>

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentUserHistoryBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {
        b.imgBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initView() {
        Utils.showLoading(requireContext())

        b.rvHistory.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        b.rvHistory.layoutManager = linearLayoutManager

        val query: Query = database.child("History").child(uid)

        options = FirebaseRecyclerOptions.Builder<History>()
            .setQuery(
                query,
                History::class.java
            )
            .build()

        adapter = object : FirebaseRecyclerAdapter<History, FirebaseViewHolder>(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                val count = adapter.itemCount
                if (count <= 0) {
                    Utils.dismissLoading()
                    b.linearEmpty.visibility = View.VISIBLE
                    b.rvHistory.visibility = View.GONE
                } else {
                    b.linearEmpty.visibility = View.GONE
                    b.rvHistory.visibility = View.VISIBLE
                }
            }

            override fun onBindViewHolder(
                holder: FirebaseViewHolder,
                i: Int,
                history: History
            ) {
                Utils.dismissLoading()
                val balance = Utils.formatRupiah(history.balance!!)
                holder.tvStatus!!.text = history.status
                holder.tvTimestamp!!.text = Utils.formatDateSimple(history.timestamp!!)

                when (history.status) {
                    "Pending" -> {
                        holder.tvStatus!!.setTextColor(Color.parseColor("#FFAB00"))
                    }

                    "Berhasil" -> {
                        holder.tvStatus!!.setTextColor(Color.parseColor("#15BE6D"))
                    }

                    "Gagal" -> {
                        holder.tvStatus!!.setTextColor(Color.parseColor("#E02020"))
                    }
                }

                if (history.sender_id == uid) {
                    Glide.with(requireContext()).load(R.drawable.ic_arrow_up).into(holder.imgType!!)
                    holder.tvType!!.text = history.type
                    holder.tvBalance!!.text = resources.getString(R.string.balance_out, balance)
                    holder.tvDesc!!.text =
                        resources.getString(R.string.pay_out, history.receive_name)
                } else {
                    Glide.with(requireContext()).load(R.drawable.ic_arrow_down)
                        .into(holder.imgType!!)
                    holder.tvType!!.text = history.type
                    holder.tvBalance!!.text = resources.getString(R.string.balance_in, balance)
                    holder.tvDesc!!.text =
                        resources.getString(R.string.pay_in, history.sender_name)
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): FirebaseViewHolder {
                return FirebaseViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_transaksi, parent, false)
                )
            }
        }

        b.rvHistory.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}