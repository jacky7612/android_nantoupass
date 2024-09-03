package com.jotangi.nantouparking.model.payment

import com.google.gson.annotations.SerializedName

data class AllPayProduct(
    @SerializedName("name")
    val name: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Int
)

