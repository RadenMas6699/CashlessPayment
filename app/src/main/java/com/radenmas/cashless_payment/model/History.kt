/*
 * Created by RadenMas on 19/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.cashless_payment.model

/**
 * Created by RadenMas on 19/08/2022.
 */

class History {

    lateinit var id: String
    lateinit var sender_id: String
    lateinit var receive_id: String
    lateinit var sender_name: String
    lateinit var receive_name: String
    lateinit var status: String
    lateinit var type: String
    var balance: Int? = null
    var timestamp: Long? = null

    constructor()

    constructor(
        id: String,
        sender_id: String,
        receive_id: String,
        sender_name: String,
        receive_name: String,
        status: String,
        type: String,
        balance: Int,
        timestamp: Long,
    ) {
        this.id = id
        this.sender_id = sender_id
        this.receive_id = receive_id
        this.sender_name = sender_name
        this.receive_name = receive_name
        this.status = status
        this.type = type
        this.balance = balance
        this.timestamp = timestamp
    }
}