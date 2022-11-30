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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.adapter.FirebaseViewHolder
import com.radenmas.cashless_payment.databinding.BottomTopUpBinding
import com.radenmas.cashless_payment.databinding.FragmentUserHomeBinding
import com.radenmas.cashless_payment.model.History
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.utils.Utils
import java.util.*


class UserHomeFragment : Fragment() {

    private lateinit var b: FragmentUserHomeBinding

    private lateinit var bs: BottomTopUpBinding
    private lateinit var dialog: BottomSheetDialog

    private lateinit var options: FirebaseRecyclerOptions<History>
    private lateinit var adapter: FirebaseRecyclerAdapter<History, FirebaseViewHolder>

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val database = FirebaseDatabase.getInstance().reference

    lateinit var username: String
    lateinit var firstname: String
    lateinit var lastname: String
    lateinit var email: String

    private var times: Long = 0
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentUserHomeBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {
        b.imgQRCode.setOnClickListener {
            Utils.showQRCode(requireContext(), uid, username)
        }

        b.imgNotif.setOnClickListener {
            findNavController().navigate(R.id.action_userHomeFragment_to_userNotificationFragment)
        }

        b.linearTopUp.setOnClickListener {
            bs = BottomTopUpBinding.inflate(layoutInflater)
            val v = bs.root

            dialog = BottomSheetDialog(requireActivity(), R.style.DialogStyle)
            dialog.setCancelable(true)
            dialog.setContentView(v)
            dialog.show()

            bs.btnTopUp.setOnClickListener {
                val topup = bs.etTopUp.text.toString()

                if (topup.isNotBlank()) {
                    times = System.currentTimeMillis()
                    id = "$uid-$times"

                    val isiSaldo = topup.toInt()

                    if (isiSaldo >= 10000) {
                        dialog.dismiss()

                        SdkUIFlowBuilder.init()
                            .setClientKey(com.radenmas.cashless_payment.BuildConfig.CLIENT_KEY)
                            .setContext(requireContext())
                            .setMerchantBaseUrl(com.radenmas.cashless_payment.BuildConfig.MERCHANT_BASE_URL)
                            .enableLog(false)
                            .setColorTheme(
                                CustomColorTheme(
                                    "#FFE51255", "#FFB61548", "#FFE51255"
                                )
                            )
                            .setLanguage("id")
                            .buildSDK()

                        val transactionRequest = TransactionRequest(
                            id, topup.toDouble()
                        )

                        val customerDetails = CustomerDetails()
                        customerDetails.customerIdentifier = uid
                        customerDetails.phone = "081234567890"
                        customerDetails.firstName = firstname
                        customerDetails.lastName = lastname
                        customerDetails.email = email

                        val shippingAddress = ShippingAddress()
                        shippingAddress.address = "Parang Tambung"
                        shippingAddress.city = "Makassar"
                        shippingAddress.postalCode = "90224"
                        customerDetails.shippingAddress = shippingAddress

                        val billingAddress = BillingAddress()
                        billingAddress.address = "Parang Tambung"
                        billingAddress.city = "Makassar"
                        billingAddress.postalCode = "90224"
                        customerDetails.billingAddress = billingAddress

                        transactionRequest.customerDetails = customerDetails

                        MidtransSDK.getInstance().transactionRequest = transactionRequest
                        MidtransSDK.getInstance().startPaymentUiFlow(requireContext())
                    } else {
                        bs.etTopUp.error = "Minimal isi saldo Rp10.000"
                    }
                } else {
                    Utils.toast(requireContext(), "Masukkan nominal isi saldo")
                }
            }
        }

        b.linearHistory.setOnClickListener {
            findNavController().navigate(R.id.action_userHomeFragment_to_userHistoryFragment)
        }
    }

    private fun initView() {
        Utils.showLoading(requireContext())

        database.child("User").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!

                    Glide.with(requireContext()).load(user.profile)
                        .placeholder(R.drawable.ic_profile_default).into(b.imgProfile)

                    username = user.username
                    email = user.email

                    val split = username.split(" ")

                    firstname = split[0]
                    lastname = split[split.size - 1]

                    b.tvUsername.text = user.username
                    b.tvBalance.text = Utils.formatNumber(user.balance!!)

                    when (user.role) {
                        "admin" -> b.imgQRCode.visibility = View.VISIBLE
                        else -> {
                            b.imgQRCode.visibility = View.GONE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        b.rvHistory.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        b.rvHistory.layoutManager = linearLayoutManager

        val query: Query = database.child("History").child(uid).limitToLast(5)

        options = FirebaseRecyclerOptions.Builder<History>().setQuery(
            query, History::class.java
        ).build()

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
                holder: FirebaseViewHolder, i: Int, history: History
            ) {
                Utils.dismissLoading()
                val balance = Utils.formatRupiah(history.balance!!)
                holder.tvStatus!!.text = history.status
                holder.tvType!!.text = history.type
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
                    holder.tvBalance!!.text = resources.getString(R.string.balance_out, balance)
                    holder.tvDesc!!.text =
                        resources.getString(R.string.pay_out, history.receive_name)
                } else {
                    Glide.with(requireContext()).load(R.drawable.ic_arrow_down)
                        .into(holder.imgType!!)
                    holder.tvBalance!!.text = resources.getString(R.string.balance_in, balance)
                    holder.tvDesc!!.text = resources.getString(R.string.pay_in, history.sender_name)
                }
            }

            override fun onCreateViewHolder(
                parent: ViewGroup, viewType: Int
            ): FirebaseViewHolder {
                return FirebaseViewHolder(
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.list_transaksi, parent, false)
                )
            }
        }

        b.rvHistory.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        b.rvHistory.adapter = adapter
        adapter.notifyDataSetChanged()
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