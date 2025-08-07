package com.jotangi.nantoupass.model.payment

import com.google.gson.annotations.SerializedName

data class AllPayHeader(
    @SerializedName("Channel")
    val channel: String,

    @SerializedName("AllPayMchID")
    val allPayMchId: String,

    @SerializedName("MchID")
    val MchId: String,

    @SerializedName("TID")
    val TId: String,

    @SerializedName("TradeKey")
    val tradeKey: String,

    @SerializedName("TransTime")
    val transTime: String


)

