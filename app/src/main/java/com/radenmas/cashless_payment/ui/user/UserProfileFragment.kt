/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.FragmentUserProfileBinding
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.ui.auth.AuthActivity

class UserProfileFragment : Fragment() {

    private var _b: FragmentUserProfileBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {

        b.btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val myAccount = requireContext().getSharedPreferences(
                "myAccount",
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = myAccount.edit()
            editor.clear().apply()

            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun initView() {
//        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
//        FirebaseDatabase.getInstance().reference.child("User").child(uid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val user = snapshot.getValue(User::class.java)!!
//                    if (user.profile == "default") {
//                        Glide.with(requireContext())
//                            .load(R.drawable.ic_profile_default)
//                            .into(b.imgProfile)
//                    } else {
//                        Glide.with(requireContext())
//                            .load(user.profile)
//                            .into(b.imgProfile)
//                    }
//                    b.tvUsername.text = user.username
//                    b.tvUsername.text = user.username
//                    b.tvEmail.text = user.email
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}