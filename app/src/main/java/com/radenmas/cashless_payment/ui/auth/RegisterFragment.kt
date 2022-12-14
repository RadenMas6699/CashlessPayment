/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.radenmas.cashless_payment.databinding.FragmentRegisterBinding
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.utils.Utils

class RegisterFragment : Fragment() {

    private lateinit var b: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()
        return view
    }

    private fun onClick() {
        b.btnRegister.setOnClickListener {
            val username: String = b.etUsername.text.toString().trim()
            val email: String = b.etEmail.text.toString().trim()
            val password: String = b.etPassword.text.toString().trim()
            val repeatPassword: String = b.etRepeatPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Utils.toast(requireContext(), "Lengkapi yang masih kosong")
            } else if (password != repeatPassword) {
                Utils.toast(requireContext(), "Kata sandi tidak sama")
            } else {
                Utils.showLoading(requireContext())
                register(username, email, password)
            }
        }

        b.tvLogin.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun register(username: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid: String = it.user!!.uid

                val dataUser = User(uid, username, email, password, "default", 0, "user")
                FirebaseDatabase.getInstance().reference.child("User").child(uid).setValue(dataUser)
                    .addOnSuccessListener {
                        Utils.toast(requireContext(), "Registrasi berhasil")
                        Utils.dismissLoading()
                        activity?.onBackPressed()
                    }
            }.addOnFailureListener {
                Log.d("XXX", it.message.toString())
                when (it.message.toString()) {
                    "The email address is already in use by another account." -> Utils.toast(
                        requireContext(),
                        "Email telah terdaftar"
                    )
                    "The given password is invalid. [ Password should be at least 6 characters ]" -> Utils.toast(
                        requireContext(),
                        "Password minimal 6 karakter"
                    )
                    else -> Utils.toast(requireContext(), "Registrasi gagal")
                }
                Utils.dismissLoading()
            }
    }

    private fun initView() {

    }
}