/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.radenmas.cashless_payment.databinding.FragmentUserPayBinding

class UserCreateFragment : Fragment() {

    private var _b: FragmentUserPayBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentUserPayBinding.inflate(inflater, container, false)
        val view = b.root

        initView()
        onClick()

        return view
    }

    private fun onClick() {

    }

    private fun initView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}