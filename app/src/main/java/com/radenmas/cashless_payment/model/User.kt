/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.model

/**
 * Created by RadenMas on 19/08/2022.
 */

class User {
    lateinit var uid: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var profile: String
    var balance: Int = 0
    lateinit var role: String

    constructor() {}

    constructor(
        uid: String,
        username: String,
        email: String,
        password: String,
        profile: String,
        balance: Int,
        role: String
    ) {
        this.uid = uid
        this.username = username
        this.email = email
        this.password = password
        this.profile = profile
        this.balance = balance
        this.role = role
    }
}