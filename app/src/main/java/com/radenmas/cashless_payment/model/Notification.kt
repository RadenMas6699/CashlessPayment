/*
 * Created by RadenMas on 19/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.model

/**
 * Created by RadenMas on 19/08/2022.
 */

class Notification {
    var title: String = ""
    var body: String = ""
    var timestamp: Long = 0
    var read: Boolean = false

    constructor()

    constructor(
        title: String,
        body: String,
        timestamp: Long,
        read: Boolean
    ) {
        this.title = title
        this.body = body
        this.timestamp = timestamp
        this.read = read
    }
}