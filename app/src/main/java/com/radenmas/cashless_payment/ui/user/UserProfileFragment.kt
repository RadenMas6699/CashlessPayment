/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import com.radenmas.cashless_payment.R
import com.radenmas.cashless_payment.databinding.FragmentUserProfileBinding
import com.radenmas.cashless_payment.model.User
import com.radenmas.cashless_payment.ui.auth.AuthActivity
import com.radenmas.cashless_payment.utils.Utils

class UserProfileFragment : Fragment() {

    private lateinit var b: FragmentUserProfileBinding

    private val RESULT_OK = -1
    private var filePath: Uri? = null

    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {
        b.imgUpdateProfile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 71)
        }

        b.btnLogout.setOnClickListener {
            Firebase.auth.signOut()

            Firebase.messaging.unsubscribeFromTopic(uid)

            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 71 && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            uploadFoto()
        }
    }

    private fun uploadFoto() {
        Utils.showLoading(requireContext())
        if (filePath != null) {
            val storageReference =
                FirebaseStorage.getInstance().getReference("User").child(uid)
            val ref = storageReference.child("profile")
            ref.putFile(filePath!!).addOnSuccessListener {

                ref.downloadUrl.addOnSuccessListener { uri: Uri ->

                    Glide.with(requireContext()).load(uri.toString())
                        .placeholder(R.drawable.ic_profile_default)
                        .into(b.imgProfile)

                    val dataUser: MutableMap<String, Any> = HashMap()
                    dataUser["profile"] = uri.toString()
                    database.child("User").child(uid)
                        .updateChildren(dataUser).addOnSuccessListener {
                            Utils.dismissLoading()
                            Utils.toast(requireContext(), "Foto profil berhasil diubah")
                        }
                }
            }
        } else {
            Utils.dismissLoading()
            Utils.toast(requireContext(), "Gambar belum dipilih")
        }
    }

    private fun initView() {
        database.child("User").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!

                    Glide.with(requireContext()).load(user.profile)
                        .placeholder(R.drawable.ic_profile_default)
                        .into(b.imgProfile)

                    b.tvUsernameTitle.text = user.username
                    b.tvUsername.text = user.username
                    b.tvEmail.text = user.email
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}