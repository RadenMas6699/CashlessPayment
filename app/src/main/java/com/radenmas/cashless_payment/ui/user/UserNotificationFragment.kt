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
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.adapter.FirebaseViewHolder
import com.radenmas.cashless_payment.databinding.FragmentUserNotificationBinding
import com.radenmas.cashless_payment.model.Notification
import com.radenmas.cashless_payment.utils.Utils

class UserNotificationFragment : Fragment() {

    private lateinit var b: FragmentUserNotificationBinding

    private lateinit var options: FirebaseRecyclerOptions<Notification>
    private lateinit var adapter: FirebaseRecyclerAdapter<Notification, FirebaseViewHolder>

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentUserNotificationBinding.inflate(inflater, container, false)
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

        b.rvNotification.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        b.rvNotification.layoutManager = linearLayoutManager

        val query: Query = database.child("Notification").child(uid)

        options = FirebaseRecyclerOptions.Builder<Notification>()
            .setQuery(
                query,
                Notification::class.java
            )
            .build()

        adapter = object : FirebaseRecyclerAdapter<Notification, FirebaseViewHolder>(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                val count = adapter.itemCount
                if (count <= 0) {
                    Utils.dismissLoading()
                    b.linearEmpty.visibility = View.VISIBLE
                    b.rvNotification.visibility = View.GONE
                } else {
                    b.linearEmpty.visibility = View.GONE
                    b.rvNotification.visibility = View.VISIBLE
                }
            }

            override fun onBindViewHolder(
                holder: FirebaseViewHolder,
                i: Int,
                notification: Notification
            ) {
                Utils.dismissLoading()

                if (notification.read) {
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.white))
                } else {
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.primary_light2))
                }
                holder.tvType!!.text = notification.title
                holder.tvDesc!!.text = notification.body
                holder.tvTimestamp!!.text = Utils.formatDateSimple(notification.timestamp!!)

                holder.itemView.setOnClickListener {
                    val dataNotif: MutableMap<String, Any> = HashMap()
                    dataNotif["read"] = true
                    FirebaseDatabase.getInstance().getReference("Notification").child(uid)
                        .child(notification.timestamp.toString())
                        .updateChildren(dataNotif)
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): FirebaseViewHolder {
                return FirebaseViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.list_notification, parent, false)
                )
            }
        }

        b.rvNotification.adapter = adapter
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