/*
 * Created by RadenMas on 5/10/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.model

/**
 * Created by RadenMas on 19/08/2022.
 */

class User {
    var uid: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var profile: String = ""
    var balance: Int? = null
    var role: String = ""

    constructor()

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