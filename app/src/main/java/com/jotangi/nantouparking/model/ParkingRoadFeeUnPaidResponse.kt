package com.jotangi.nantouparking.model

import com.google.gson.annotations.SerializedName

data class ParkingRoadFeeUnPaidResponse(
    @SerializedName("data")
    val unPaidItems: List<ParkingRoadFeeUnPaidVO>,
    @SerializedName("status")
    val status: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("responseMessage")
    val responseMessage: String

)

data class ParkingRoadFeeUnPaidResponse2(
    @SerializedName("data")
    val unPaidItems: List<ParkingRoadFeeUnPaidVO2>,

    @SerializedName("responseMessage")
    val responseMessage: String

)