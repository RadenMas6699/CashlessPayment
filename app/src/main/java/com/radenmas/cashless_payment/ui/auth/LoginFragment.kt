/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.FragmentLoginBinding
import com.radenmas.cashless_payment.ui.user.UserMainActivity
import com.radenmas.cashless_payment.utils.Utils

class LoginFragment : Fragment() {

    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentLoginBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()
        return view
    }

    private fun onClick() {

        b.btnLogin.setOnClickListener {
            val email: String = b.etEmail.text.toString().trim()
            val password: String = b.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Lengkapi yang masih kosong", Toast.LENGTH_SHORT).show()
            } else {
                Utils.showLoading(requireContext())
                login(email, password)
            }
        }

        b.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = it.user!!.uid
                FirebaseDatabase.getInstance().reference.child("User").child(uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val role = snapshot.child("role").value.toString().trim()

                            val myAccount = requireContext().getSharedPreferences(
                                "myAccount",
                                AppCompatActivity.MODE_PRIVATE
                            )
                            val editor = myAccount.edit()
                            editor.putString("role", role)
                            editor.apply()

                            Utils.toast(requireContext(), "Login berhasil")
                            Utils.dismissLoading()

                            if (role == "user") {
                                startActivity(Intent(context, UserMainActivity::class.java))
                            }
//                            else {
//                                startActivity(Intent(context, AdminActivity::class.java))
//                            }
                            activity?.finish()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }.addOnFailureListener {
                Utils.toast(requireContext(), it.toString())
                Utils.dismissLoading()
            }
    }

    private fun initView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}