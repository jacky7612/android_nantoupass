package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingFeePaidDetailVO(
    @SerializedName("plateNo")
    val plateNo: String,

    @SerializedName("bill_no")
    val billNo: String,

    @SerializedName("bill_updated_at")
    val billPayDate: String,

    @SerializedName("bill_amount")
    val billAmount: String,

    @SerializedName("pay_type")
    val billPayType: String,

    @SerializedName("pay_status")
    val billPayStatus: String,

    @SerializedName("recipientaddr")
    val billAddress: String,

    @SerializedName("description")
    val billDescription: String,

    @SerializedName("bill_created_at")
    val billCreateTime: String
)