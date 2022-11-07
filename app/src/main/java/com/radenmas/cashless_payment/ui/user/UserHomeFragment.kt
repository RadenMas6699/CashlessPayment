/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.annotation.SuppressLint
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
import com.google.firebase.database.*
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.adapter.FirebaseViewHolder
import com.radenmas.cashless_payment.databinding.FragmentUserHomeBinding
import com.radenmas.cashless_payment.model.History
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.utils.Utils

class UserHomeFragment : Fragment() {

    private var _b: FragmentUserHomeBinding? = null
    private val b get() = _b!!

    private lateinit var options: FirebaseRecyclerOptions<History>
    private lateinit var adapter: FirebaseRecyclerAdapter<History, FirebaseViewHolder>

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentUserHomeBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {
        b.imgNotif.setOnClickListener {

        }

    }

    private fun initView() {

        database.child("User").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)

                        Glide.with(requireContext())
                            .load(user!!.profile)
                            .placeholder(R.drawable.ic_profile_default)
                            .into(b.imgProfile)

                        b.tvUsername.text = user.username
                        b.tvBalance.text = Utils.formatNumber(user.balance!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        b.rvHistory.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        b.rvHistory.layoutManager = linearLayoutManager

        val query: Query = database.child("History").child(uid).limitToLast(3)

        options = FirebaseRecyclerOptions.Builder<History>()
            .setQuery(
                query,
                History::class.java
            )
            .build()

        adapter = object : FirebaseRecyclerAdapter<History, FirebaseViewHolder>(options) {
            override fun onBindViewHolder(
                holder: FirebaseViewHolder,
                i: Int,
                history: History
            ) {
                val balance = Utils.formatRupiah(history.balance!!)

                if (history.sender_id == uid) {
                    holder.tvType!!.text = resources.getString(R.string.payment)
                    holder.tvBalance!!.text = resources.getString(R.string.balance_out, balance)
                    holder.tvDesc!!.text =
                        resources.getString(R.string.pay_out, history.receive_name)
                } else {
                    holder.tvType!!.text = resources.getString(R.string.top_up)
                    holder.tvBalance!!.text = resources.getString(R.string.balance_in, balance)
                    holder.tvDesc!!.text =
                        resources.getString(R.string.pay_in, history.receive_name)
                }

                holder.tvStatus!!.text = history.status
                holder.tvTimestamp!!.text = Utils.formatDateSimple(history.timestamp!!)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}