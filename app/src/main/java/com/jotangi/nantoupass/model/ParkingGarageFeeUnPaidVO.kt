package com.jotangi.nantoupass.model

import com.google.gson.annotations.SerializedName

data class ParkingGarageFeeUnPaidVO(
    @SerializedName("order_no")
    val billNo: String,

    @SerializedName("plate_no")
    val billPlateNo: String,

    @SerializedName("start_time")
    val billStartTime: String,

    @SerializedName("amount")
    val billAmount: String,

    @SerializedName("stay_time")
    val billStayTime: String,

    @SerializedName("image_path")
    val billImagePath: String,

    @SerializedName("searchTime")
    val billSearchTime: String,

    var isSelected: Boolean = false,

    var isLocked: Boolean = false,

    var lockDueTime: Long = 0
)