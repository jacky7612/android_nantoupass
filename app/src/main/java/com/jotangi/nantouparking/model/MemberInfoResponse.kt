package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class MemberInfoResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("responseMessage")
    val responseMessage: MemberInfoVO
)
