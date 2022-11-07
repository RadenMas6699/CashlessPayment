/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by RadenMas on 29/08/2022.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}