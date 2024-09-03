package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class LockBillResponse(
    @SerializedName("bill_id")
    val billId: String,

    @SerializedName("due_time")
    val dueTime: String,

    @SerializedName("time")
    val createTime: String
)