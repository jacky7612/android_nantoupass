package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(
    @SerializedName("status")
    var status: String,

    @SerializedName("code")
    var code: String,

    @SerializedName("responseMessage")
    var responseMessage: String
)
