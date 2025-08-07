package com.jotangi.nantoupass.model

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

data class ParkingRoadFeeUnPaidVO2(
    @SerializedName("amount")
    val amount: String,

    @SerializedName("parkTime")
    val parkTime: String,

    @SerializedName("enterTime")
    val enterTime: String,

    @SerializedName("exitTime")
    val exitTime: String,

    @SerializedName("discount")
    val discount: String,

    @SerializedName("road")
    val road: String,

    @SerializedName("cell")
    val cell: String,

    @SerializedName("enteredImage")
    val enteredImage: String,

    @SerializedName("exitedImage")
    val exitedImage: String,

    @SerializedName("order_no")
    val order_no: String,

    @SerializedName("year")
    val year: String,

    @SerializedName("searchTime")
    val searchTime: String,

    var isSelected: Boolean = false,

    var isLocked: Boolean = false,

    var lockDueTime: Long = 0

)