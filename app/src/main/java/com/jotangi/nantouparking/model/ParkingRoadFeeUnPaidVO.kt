package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingRoadFeeUnPaidVO(
    @SerializedName("ticket")
    val billNo: String,

    @SerializedName("plateNo")
    val billPlateNo: String,

    @SerializedName("parkTime")
    val billStartTime: String,

    @SerializedName("leaveTime")
    val billLeaveTime: String,

    @SerializedName("amount")
    val billAmount: String,

    @SerializedName("road")
    val billRoad: String,

    @SerializedName("cell")
    val billCell: String,

    @SerializedName("imagePath")
    val billImagePath: String,

    @SerializedName("searchTime")
    val billSearchTime: String,
//
//    @SerializedName("serial")
//    val billNo: String,
//
//    @SerializedName("amount")
//    val billAmount: String,
//
//    @SerializedName("date")
//    val billDate: String,
//
//    @SerializedName("due_date")
//    val billDueDate: String,
//
//    @SerializedName("description")
//    val parkingPositionInfo: String,

    var isSelected: Boolean = false,

    var isLocked: Boolean = false,

    var lockDueTime: Long = 0
)