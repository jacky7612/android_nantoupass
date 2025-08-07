package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class AddPlateNoResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("responseMessage")
    val responseMessage: String
)
