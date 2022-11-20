/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.FragmentLoginBinding
import com.radenmas.cashless_payment.ui.user.UserMainActivity
import com.radenmas.cashless_payment.utils.Utils

class LoginFragment : Fragment() {

    private lateinit var b: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentLoginBinding.inflate(inflater, container, false)
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
                Utils.toast(requireContext(), "Login berhasil")
                Utils.dismissLoading()

                startActivity(Intent(context, UserMainActivity::class.java))

                activity?.finish()
            }.addOnFailureListener {
                Log.d("XXX", it.message.toString())
                when (it.message.toString()) {
                    "There is no user record corresponding to this identifier. The user may have been deleted." -> Utils.toast(
                        requireContext(), "Email tidak terdaftar"
                    )
                    "The password is invalid or the user does not have a password." -> Utils.toast(
                        requireContext(), "Password salah"
                    )
                    else -> Utils.toast(requireContext(), "Login gagal")
                }
                Utils.dismissLoading()
            }
    }

    private fun initView() {

    }
}