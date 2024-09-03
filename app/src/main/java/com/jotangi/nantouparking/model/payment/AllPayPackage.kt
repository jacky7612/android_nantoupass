package com.jotangi.nantouparking.model.payment

import com.google.gson.annotations.SerializedName

data class AllPayPackage(
    @SerializedName("Header")
    val header: AllPayHeader,

    @SerializedName("Data")
    val data: String
)

