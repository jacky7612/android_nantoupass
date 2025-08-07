package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status")
    var status: String = "",

    @SerializedName("code")
    var code: String = "",

    @SerializedName("responseMessage")
    var responseMessage: String = ""
)
