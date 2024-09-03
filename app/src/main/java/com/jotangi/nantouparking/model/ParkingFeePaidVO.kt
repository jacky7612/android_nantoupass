package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingFeePaidVO(
    @SerializedName("bill_no")
    val orderNo: String,

    @SerializedName("plateNo")
    val plateNo: String,

    @SerializedName("bill_updated_at")
    val orderPayDate: String,

    @SerializedName("bill_amount")
    val orderPayAmount: String,

    @SerializedName("pay_status")
    val billPayStatus: String
)


data class GovParkingFeePaidVO(
    @SerializedName("ticket")
    val ticket: String,

    @SerializedName("area")
    val area: String,

    @SerializedName("parkDate")
    val parkDate: String,

    @SerializedName("payAmount")
    val payAmount: String,

    @SerializedName("payDate")
    val payDate: String,

    @SerializedName("paySource")
    val paySource: String
)