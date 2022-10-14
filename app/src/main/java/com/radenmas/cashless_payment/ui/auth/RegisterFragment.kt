/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.radenmas.cashless_payment.databinding.FragmentRegisterBinding
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.utils.Utils

class RegisterFragment : Fragment() {

    private var _b: FragmentRegisterBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()
        return view
    }

    private fun onClick() {
        b.btnRegister.setOnClickListener {
            val username: String = b.etUsername.text.toString()
            val email: String = b.etEmail.text.toString()
            val password: String = b.etPassword.text.toString()



            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Lengkapi yang masih kosong", Toast.LENGTH_SHORT).show()
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

                val dataUser = User(uid, username, email, password, "default", "","user")
                FirebaseDatabase.getInstance().reference.child("User").child(uid).setValue(dataUser)
                Utils.toast(requireContext(), "Registrasi berhasil")
                Utils.dismissLoading()
                activity?.onBackPressed()
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