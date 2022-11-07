/*
 * Created by RadenMas on 19/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.model

/**
 * Created by RadenMas on 19/08/2022.
 */

data class History2(
    var id: String,
    var sender_id: String,
    var receive_id: String,
    var sender_name: String,
    var receive_name: String,
    var status: String,
    var balance: Int? = null,
    var timestamp: Long? = null
)
